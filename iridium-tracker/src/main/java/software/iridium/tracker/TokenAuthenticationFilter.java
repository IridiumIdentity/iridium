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
package software.iridium.tracker;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.entity.user.PrincipalUser;

public class TokenAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

  private WebClient webClient;

  public static final String BEARER_PREFIX_WITH_SPACE = "Bearer ";

  public TokenAuthenticationFilter(
      final AuthenticationManager authenticationManager, final WebClient webClient) {
    super.setAuthenticationManager(authenticationManager);
    this.webClient = webClient;
  }

  @Override
  protected Object getPreAuthenticatedPrincipal(final HttpServletRequest httpServletRequest) {

    String token = extractBearerToken(httpServletRequest);

    if (StringUtils.isNotBlank(token)) {
      final var response =
          webClient
              .method(HttpMethod.GET)
              .uri("http://localhost:8381/identities")
              .header("Authorization", "Bearer " + token)
              .header("Accept", IdentityResponse.MEDIA_TYPE)
              .retrieve()
              .bodyToMono(IdentityResponse.class)
              .onErrorResume(Mono::error)
              .block();

      return new PrincipalUser(token, response.getId(), List.of());
    }
    return null;
  }

  private String extractBearerToken(final HttpServletRequest request) {
    final var header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header != null && header.length() > BEARER_PREFIX_WITH_SPACE.length()) {
      return header.substring(BEARER_PREFIX_WITH_SPACE.length());
    }
    return null;
  }

  @Override
  protected Object getPreAuthenticatedCredentials(final HttpServletRequest request) {
    return "";
  }
}
