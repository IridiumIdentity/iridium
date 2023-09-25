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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import software.iridium.api.repository.AccessTokenEntityRepository;
import software.iridium.api.user.PrincipalUser;
import software.iridium.entity.AccessTokenEntity;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {

  @Mock private AccessTokenEntityRepository mockTokenRepository;

  @Mock private HttpServletRequest mockServletRequest;
  @InjectMocks private TokenAuthenticationFilter subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockTokenRepository, mockServletRequest);
  }

  @Test
  public void getPreAuthenticatedPrincipal_AllGood_BehavesAsExpected() {
    final var accessTokenValue = "Bearer the token value";
    final var username = "the identity username";
    final var accessToken = new AccessTokenEntity();
    accessToken.setAccessToken(accessTokenValue);
    accessToken.setIdentityId(username);

    when(mockServletRequest.getHeader(eq(HttpHeaders.AUTHORIZATION))).thenReturn(accessTokenValue);
    when(mockTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            eq("the token value"), any(Date.class)))
        .thenReturn(Optional.of(accessToken));

    final var response = subject.getPreAuthenticatedPrincipal(mockServletRequest);

    verify(mockServletRequest).getHeader(eq(HttpHeaders.AUTHORIZATION));
    verify(mockTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(eq("the token value"), any(Date.class));

    final var principalUser = (PrincipalUser) response;

    assertThat(principalUser.getAuthToken(), is(equalTo(accessTokenValue)));
    assertThat(principalUser.getName(), is(equalTo(username)));
  }

  @Test
  public void getPreAuthenticatedPrincipal_BasicAuth_BehavesAsExpected() {
    final var accessTokenValue = "Basic theEncodedTokenValue";
    final var exchangePath = "/oauth/token";

    when(mockServletRequest.getHeader(eq(HttpHeaders.AUTHORIZATION))).thenReturn(accessTokenValue);

    when(mockServletRequest.getServletPath()).thenReturn(exchangePath);

    assertThat(subject.getPreAuthenticatedPrincipal(mockServletRequest), nullValue());

    verify(mockServletRequest).getHeader(eq(HttpHeaders.AUTHORIZATION));
    verify(mockServletRequest).getServletPath();
    verify(mockTokenRepository, never())
        .findFirstByAccessTokenAndExpirationAfter(anyString(), any(Date.class));
  }

  @Test
  public void getPreAuthenticatedCredentials_AllGood_ReturnsAsExpected() {

    assertThat(subject.getPreAuthenticatedCredentials(mockServletRequest), is(equalTo("")));
  }
}
