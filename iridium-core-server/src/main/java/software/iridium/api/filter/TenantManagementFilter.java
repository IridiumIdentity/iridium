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
package software.iridium.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.repository.AccessTokenEntityRepository;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.entity.TenantEntity;

@Component
@Order(1)
public class TenantManagementFilter extends OncePerRequestFilter {

  private static final String BEARER_PREFIX_WITH_SPACE = "Bearer ";
  private static final Logger logger = LoggerFactory.getLogger(TenantManagementFilter.class);

  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private AccessTokenEntityRepository accessTokenRepository;
  @Autowired private IdentityEntityRepository identityRepository;

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain)
      throws ServletException, IOException {
    final var path = getRequestPath(request);

    if (path.contains("tenant")) {
      logger.info("tenant operation ::: need to verify ownership");
      final var firstSegmentMarkerIndex = path.indexOf("/");
      final var secondSegmentMarkerIndex = path.indexOf("/", firstSegmentMarkerIndex + 1);
      if (firstSegmentMarkerIndex != -1 && secondSegmentMarkerIndex != -1) {
        final var tenantId = path.substring(firstSegmentMarkerIndex + 1, secondSegmentMarkerIndex);
        boolean isManagedTenant = verifyOwnershipClaim(request, tenantId);
        if (!isManagedTenant) {
          throw new NotAuthorizedException();
        }
      } else {
        logger.info("pulling tenant info in other way");
      }
    }
    filterChain.doFilter(request, response);
  }

  protected boolean verifyOwnershipClaim(final HttpServletRequest request, final String tenantId) {

    final var tenant = tenantRepository.findById(tenantId).orElseThrow(NotAuthorizedException::new);
    logger.info("verifying ownership of tenant: {}", tenant.getSubdomain());
    final var bearerToken = extractBearerToken(request);

    final var accessToken =
        accessTokenRepository
            .findFirstByAccessTokenAndExpirationAfter(bearerToken, new Date())
            .orElseThrow(NotAuthorizedException::new);
    final var identity =
        identityRepository
            .findById(accessToken.getIdentityId())
            .orElseThrow(NotAuthorizedException::new);
    var isManagedTenant = false;
    for (TenantEntity managedTenant : identity.getManagedTenants()) {
      if (managedTenant.getId().equals(tenantId)) {
        isManagedTenant = true;
        break;
      }
    }
    return isManagedTenant;
  }

  private String getRequestPath(HttpServletRequest request) {
    String url = request.getRequestURL().toString();
    int pathSegmentMarker = StringUtils.ordinalIndexOf(url, "/", 3);
    return url.substring(pathSegmentMarker + 1);
  }

  private String extractBearerToken(final HttpServletRequest request) {
    final var header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header != null && header.length() > BEARER_PREFIX_WITH_SPACE.length()) {
      return header.substring(BEARER_PREFIX_WITH_SPACE.length());
    }
    return null;
  }
}
