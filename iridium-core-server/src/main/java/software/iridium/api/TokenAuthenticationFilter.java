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
package software.iridium.api;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.entity.AccessTokenEntity;
import software.iridium.api.repository.AccessTokenEntityRepository;

public class TokenAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

  private AccessTokenEntityRepository tokenEntityRepository;

  public static final String BEARER_PREFIX_WITH_SPACE = "Bearer ";
  private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

  public TokenAuthenticationFilter(
      final AuthenticationManager authenticationManager,
      final AccessTokenEntityRepository accessTokenEntityRepository) {
    super.setAuthenticationManager(authenticationManager);
    this.tokenEntityRepository = accessTokenEntityRepository;
  }

  @Override
  protected Object getPreAuthenticatedPrincipal(final HttpServletRequest httpServletRequest) {
    //      if (Objects.equals(httpServletRequest.getPathInfo(), "/identities")) {
    //          return null;
    //      }
    String token = extractBearerToken(httpServletRequest);

    if (StringUtils.isNotBlank(token)) {
      // verify bearer token
      // get user details
      // return new Principal User
      // final var firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
      final AccessTokenEntity entity =
          tokenEntityRepository
              .findFirstByAccessTokenAndExpirationAfter(token, new Date())
              .orElseThrow(NotAuthorizedException::new);

      //          final var request = HttpRequest.newBuilder()
      //                  .uri(new URI("http://localhost:8381/identities"))
      //                  .headers("Accept", "application/vnd.iridium.id.identity-response.1+json",
      // HttpHeaders.AUTHORIZATION, httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION))
      //                  .timeout(Duration.of(10, SECONDS))
      //                  .GET()
      //                  .build();
      //
      //          HttpResponse<String> response = HttpClient.newBuilder()
      //                  .build()
      //                  .send(request, HttpResponse.BodyHandlers.ofString());
      //          logger.info("response was: " + response.body());
      //          ObjectMapper mapper = new ObjectMapper();
      //          identityResponse = mapper.readValue(response.body(), IdentityResponse.class);
      return new PrincipalUser(entity.getAccessToken(), entity.getIdentityId(), List.of());
    }
    return null;
  }

  private String extractBearerToken(final HttpServletRequest request) {
    final var header = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (header != null && !header.isBlank()) {
      return header.substring(BEARER_PREFIX_WITH_SPACE.length());
    }
    return null;
  }

  @Override
  protected Object getPreAuthenticatedCredentials(final HttpServletRequest request) {
    return "";
  }
}
