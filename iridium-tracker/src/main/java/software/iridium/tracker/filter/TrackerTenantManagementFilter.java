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
package software.iridium.tracker.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.base.error.NotAuthorizedException;

@Component
@Order(1)
public class TrackerTenantManagementFilter extends OncePerRequestFilter {

  @Autowired private WebClient webClient;

  private String coreServerBaseURL;

  private static final String BEARER_PREFIX_WITH_SPACE = "Bearer ";
  private static final Logger logger = LoggerFactory.getLogger(TrackerTenantManagementFilter.class);

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain)
      throws ServletException, IOException {
    final var path = getRequestPath(request);

    if (path.contains("tenant")) {
      logger.info("tracker tenant operation ::: need to verify ownership");
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
    final var bearerToken = extractBearerToken(request);

    final var response =
        webClient
            .method(HttpMethod.GET)
            .uri(
                WebApplicationContextUtils.getRequiredWebApplicationContext(
                            this.getServletContext())
                        .getEnvironment()
                        .getProperty("iridium.core.base.url")
                    + "/identities")
            .header("Authorization", "Bearer " + bearerToken)
            .header("Accept", IdentityResponse.MEDIA_TYPE)
            .retrieve()
            .bodyToMono(IdentityResponse.class)
            .block();

    return response != null && response.getTenantIds().contains(tenantId);
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
