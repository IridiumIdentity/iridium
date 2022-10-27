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

import java.util.Calendar;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.AuthenticationEntity;
import software.iridium.api.handler.NewIdentityEventHandler;
import software.iridium.api.instantiator.AuthenticationRequestInstantiator;
import software.iridium.api.instantiator.IdentityCreateRequestDetailsInstantiator;
import software.iridium.api.instantiator.IdentityEntityInstantiator;
import software.iridium.api.mapper.IdentityEntityMapper;
import software.iridium.api.mapper.IdentityResponseMapper;
import software.iridium.api.repository.*;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.ServletTokenExtractor;

@Service
public class IdentityService {

  public static final Integer TEMP_PASSWORD_LENGTH = 8;

  @Resource private AuthenticationEntityRepository authenticationRepository;
  @Resource private IdentityEntityMapper identityEntityMapper;
  @Resource private IdentityEntityInstantiator identityInstantiator;
  @Resource private IdentityEntityRepository identityRepository;
  @Resource private IdentityResponseMapper responseMapper;
  @Resource private BCryptPasswordEncoder encoder;
  @Resource private NewIdentityEventHandler eventHandler;
  @Resource private IdentityEmailEntityRepository emailRepository;
  @Resource private ServletTokenExtractor tokenExtractor;
  @Resource private TenantEntityRepository tenantRepository;
  @Resource private ApplicationEntityRepository applicationRepository;
  @Resource private AttributeValidator attributeValidator;
  @Resource private IdentityCreateRequestDetailsInstantiator requestDetailsInstantiator;
  @Resource private AuthenticationService authenticationService;
  @Resource private AuthenticationRequestInstantiator authenticationRequestInstantiator;

  @Transactional(propagation = Propagation.SUPPORTS)
  public IdentityResponse getIdentity(final HttpServletRequest request) {
    final var now = Calendar.getInstance().getTime();
    final var token = tokenExtractor.extractIridiumToken(request);
    final AuthenticationEntity auth =
        authenticationRepository
            .findFirstByAuthTokenAndExpirationAfter(token, now)
            .orElseThrow(NotAuthorizedException::new);

    return identityEntityMapper.map(auth.getIdentity());
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityResponse create(
      final CreateIdentityRequest request, final Map<String, String> requestParams) {
    final var emailAddress = request.getUsername();
    checkArgument(
        EmailValidator.getInstance().isValid(emailAddress),
        String.format("email must not be blank and properly formatted: %s", request.getUsername()));
    checkArgument(
        attributeValidator.isNotBlank(request.getClientId()),
        String.format("clientId must not be blank: %s", request.getClientId()));
    // todo add password requirements
    final var application =
        applicationRepository
            .findByClientId(request.getClientId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "application not found for clientId: " + request.getClientId()));

    if (tenantRepository.findById(application.getTenantId()).isEmpty()) {
      throw new ResourceNotFoundException(
          String.format("tenant not found for id: %s", application.getTenantId()));
    }

    if (emailRepository
        .findByEmailAddressAndIdentity_ParentTenantId(emailAddress, application.getTenantId())
        .isPresent()) {
      throw new DuplicateResourceException(
          String.format(
              "Account already registered with: %s in tenant: %s",
              emailAddress, application.getTenantId()));
    }

    final var identity =
        identityRepository.save(
            identityInstantiator.instantiate(
                request, encoder.encode(request.getPassword()), application.getTenantId()));
    final var sessionDetails = requestDetailsInstantiator.instantiate(requestParams, identity);
    identity.setCreateSessionDetails(sessionDetails);

    eventHandler.handleEvent(identity, application.getClientId());
    final AuthenticationRequest authenticationRequest =
        authenticationRequestInstantiator.instantiate(request);
    final var authenticationResponse =
        authenticationService.authenticate(authenticationRequest, requestParams);
    return responseMapper.map(identity, authenticationResponse);
  }
}
