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
package software.iridium.api.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenRequestValidatorTest {
  @Mock private AttributeValidator mockAttributeValidator;
  @InjectMocks private RefreshTokenRequestValidator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockAttributeValidator);
  }

  @Test
  void refreshToken_MissingGrantType_ExceptionThrown() {
    final var clientId = "the client id";
    final var refreshToken = "jsdkjsejdjfmncs";
    final var grantType = "";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.validate(grantType, clientId, refreshToken));

    assertThat(exception.getMessage(), is(equalTo("grant_type must not be blank")));

    verify(mockAttributeValidator).isNotBlank(same(grantType));
    verify(mockAttributeValidator, never()).isNotBlank(same(clientId));
    verify(mockAttributeValidator, never()).isNotBlank(same(refreshToken));
    verify(mockAttributeValidator, never()).isNotBlankAndNoLongerThan(same(clientId), eq(32));
  }

  @Test
  void refreshToken_MissingClientId_ExceptionThrown() {
    final var clientId = "";
    final var refreshToken = "jsdkjsejdjfmncs";
    final var grantType = "refresh_token";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.validate(grantType, clientId, refreshToken));

    assertThat(exception.getMessage(), is(equalTo("client_id must not be blank")));

    verify(mockAttributeValidator).isNotBlank(same(grantType));
    verify(mockAttributeValidator).isNotBlank(same(clientId));
    verify(mockAttributeValidator, never()).isNotBlank(same(refreshToken));
  }

  @Test
  void refreshToken_MissingRefreshToken_ExceptionThrown() {
    final var clientId = "the client id";
    final var refreshToken = "";
    final var grantType = "refresh_token";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.validate(grantType, clientId, refreshToken));

    assertThat(exception.getMessage(), is(equalTo("refresh_token must not be blank")));

    verify(mockAttributeValidator).isNotBlank(same(clientId));
    verify(mockAttributeValidator).isNotBlank(same(grantType));
    verify(mockAttributeValidator).isNotBlank(same(refreshToken));
  }

  @Test
  void refreshToken_InvalidGrantType_ExceptionThrown() {
    final var clientId = "the client id";
    final var refreshToken = "jsdkjsejdjfmncs";
    final var grantType = "refresh";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.equals(anyString(), anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.validate(grantType, clientId, refreshToken));
    assertThat(
        exception.getMessage(), is(equalTo("grant_type value must be set to \"refresh_token\"")));

    verify(mockAttributeValidator).isNotBlank(same(grantType));
    verify(mockAttributeValidator).isNotBlank(same(clientId));
    verify(mockAttributeValidator).isNotBlank(same(refreshToken));
    verify(mockAttributeValidator)
        .equals(
            same(grantType),
            eq(AuthorizationCodeFlowConstants.REFRESH_TOKEN_GRANT_TYPE.getValue()));
  }
}
