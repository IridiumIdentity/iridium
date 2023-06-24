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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.authentication.domain.IdentitySummaryResponse;
import software.iridium.api.base.domain.PagedListResponse;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.mapper.IdentityEntityMapper;
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
