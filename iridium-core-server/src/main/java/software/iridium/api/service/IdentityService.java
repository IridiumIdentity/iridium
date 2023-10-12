/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api.service;

import static com.google.common.base.Preconditions.checkArgument;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Calendar;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.authentication.domain.IdentitySummaryResponse;
import software.iridium.api.base.domain.PagedListResponse;
import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.handler.VerificationManager;
import software.iridium.api.instantiator.IdentityCreateDetailsRequestInstantiator;
import software.iridium.api.instantiator.IdentityEntityInstantiator;
import software.iridium.api.mapper.IdentityEntityMapper;
import software.iridium.api.mapper.IdentityResponseMapper;
import software.iridium.api.mapper.IdentitySummaryResponseMapper;
import software.iridium.api.repository.*;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.ServletTokenExtractor;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.TenantEntity;

@Service
public class IdentityService {

  @Autowired private IdentityEntityMapper identityEntityMapper;
  @Autowired private IdentityEntityRepository identityRepository;
  @Autowired private ServletTokenExtractor tokenExtractor;
  @Autowired private AttributeValidator attributeValidator;
  @Autowired private AccessTokenEntityRepository accessTokenRepository;
  @Autowired private IdentitySummaryResponseMapper summaryResponseMapper;
  @Autowired private ApplicationEntityRepository applicationRepository;
  @Autowired private PasswordEncoder encoder;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private IdentityEntityInstantiator identityInstantiator;
  @Autowired private IdentityCreateDetailsRequestInstantiator createDetailsRequestInstantiator;
  @Autowired private IdentityEmailEntityRepository emailRepository;
  @Autowired private IdentityResponseMapper responseMapper;
  @Autowired private VerificationManager verificationManager;

  private static final Logger logger = LoggerFactory.getLogger(IdentityService.class);

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityResponse getIdentity(final HttpServletRequest request) {
    logger.info("retrieving identity " + request.getServerName());

    final var now = Calendar.getInstance().getTime();
    final var token = tokenExtractor.extractBearerToken(request);
    final var accessToken =
        accessTokenRepository
            .findFirstByAccessTokenAndExpirationAfter(token, now)
            .orElseThrow(NotAuthorizedException::new);

    final var identity =
        identityRepository
            .findById(accessToken.getIdentityId())
            .orElseThrow(NotAuthorizedException::new);

    return identityEntityMapper.map(identity);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityResponse create(
      final CreateIdentityRequest request,
      final String responseType,
      final String state,
      final String redirectUri,
      final String clientId,
      final String codeChallengeMethod,
      final String codeChallenge) {
    final var emailAddress = request.getUsername();
    checkArgument(
        EmailValidator.getInstance().isValid(emailAddress),
        String.format("email must not be blank and properly formatted: %s", request.getUsername()));
    checkArgument(
        attributeValidator.isNotBlank(clientId),
        String.format("clientId must not be blank: %s", clientId));
    // todo: check others params
    // todo add password requirements
    final var application =
        applicationRepository
            .findByClientId(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("application not found"));

    final var tenant =
        tenantRepository
            .findById(application.getTenantId())
            .orElseThrow(() -> new ResourceNotFoundException("tenant not found"));

    if (emailRepository
        .findByEmailAddressAndIdentity_ParentTenantId(emailAddress, application.getTenantId())
        .isPresent()) {
      throw new DuplicateResourceException("Account already registered");
    }

    final var identity =
        identityRepository.save(
            identityInstantiator.instantiate(
                request, encoder.encode(request.getPassword()), application.getTenantId()));
    final var sessionDetails =
        createDetailsRequestInstantiator.instantiate(
            responseType,
            state,
            redirectUri,
            clientId,
            codeChallengeMethod,
            codeChallenge,
            identity);
    identity.setCreateSessionDetails(sessionDetails);
    // handle configured event here
    verificationManager.handleNewEmailEvent(identity, application, tenant);

    return responseMapper.map(identity, redirectUri);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public PagedListResponse<IdentitySummaryResponse> getPage(
      final HttpServletRequest request,
      final String tenantId,
      final Integer page,
      final Integer size,
      final Boolean active) {
    checkArgument(
        attributeValidator.isUuid(tenantId), "tenantId must be a valid uuid: " + tenantId);
    checkArgument(
        attributeValidator.isZeroOrGreater(page),
        "page must be a zero or greater integer: " + page);
    checkArgument(attributeValidator.isPositive(size), "size must be a positive integer: " + size);
    checkArgument(
        attributeValidator.isNotNull(active), "active must be either true or false: " + active);

    final var now = Calendar.getInstance().getTime();
    final var token = tokenExtractor.extractBearerToken(request);
    final var accessToken =
        accessTokenRepository
            .findFirstByAccessTokenAndExpirationAfter(token, now)
            .orElseThrow(NotAuthorizedException::new);

    final var identity =
        identityRepository
            .findById(accessToken.getIdentityId())
            .orElseThrow(NotAuthorizedException::new);

    var isNotManagedTenant = true;
    for (TenantEntity tenant : identity.getManagedTenants()) {
      if (tenant.getId().equalsIgnoreCase(tenantId)) {
        isNotManagedTenant = false;
        break;
      }
    }

    if (isNotManagedTenant) {
      throw new NotAuthorizedException();
    }

    Page<IdentityEntity> pageOfEntityInstances =
        identityRepository.findAllByParentTenantIdAndActive(
            tenantId, active, PageRequest.of(page, size));
    final var content = pageOfEntityInstances.getContent();
    return new PagedListResponse<>(
        summaryResponseMapper.mapToSummaries(content),
        pageOfEntityInstances.getTotalPages(),
        page,
        size);
  }
}
