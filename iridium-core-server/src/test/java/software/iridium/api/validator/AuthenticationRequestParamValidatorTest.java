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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.util.AttributeValidator;

@ExtendWith(MockitoExtension.class)
class AuthenticationRequestParamValidatorTest {

  @Mock private AttributeValidator mockValidator;
  @InjectMocks private AuthenticationRequestParamValidator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockValidator);
  }

  @Test
  public void validate_AllGood_BehavesAsExpected() {
    final var responseType = "code";
    final var state = "state";
    final var clientId = "the client id";
    final var codeChallengeMethod = "the method";
    final var codeChallenge = "the code challenge";
    final var redirectUri = "the redirect URI";

    when(mockValidator.isNotBlank(anyString())).thenCallRealMethod();

    subject.validate(
        responseType, state, redirectUri, clientId, codeChallengeMethod, codeChallenge);

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockValidator).isNotBlank(same(codeChallengeMethod));
    verify(mockValidator).isNotBlank(same(codeChallenge));
    verify(mockValidator).isNotBlank(same(redirectUri));
  }

  @Test
  public void validate_ClientIdIsBlank_ExceptionThrown() {
    final var responseType = "code";
    final var state = "state";
    final var clientId = "";
    final var codeChallengeMethod = "the method";
    final var codeChallenge = "the code challenge";
    final var redirectUri = "the redirect URI";

    when(mockValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                subject.validate(
                    responseType,
                    state,
                    redirectUri,
                    clientId,
                    codeChallengeMethod,
                    codeChallenge));

    assertThat(exception.getMessage(), is(equalTo("clientId must not be blank")));

    verify(mockValidator).isNotBlank(eq(""));
    verify(mockValidator, never()).isNotBlank(same(codeChallengeMethod));
    verify(mockValidator, never()).isNotBlank(same(codeChallenge));
    verify(mockValidator, never()).isNotBlank(same(redirectUri));
  }

  @Test
  public void validate_CodeChallengeMethodIsBlank_ExceptionThrown() {
    final var responseType = "code";
    final var state = "state";
    final var clientId = "the client id";
    final String codeChallengeMethod = null;
    final var codeChallenge = "the code challenge";
    final var redirectUri = "the redirect URI";

    when(mockValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                subject.validate(
                    responseType,
                    state,
                    redirectUri,
                    clientId,
                    codeChallengeMethod,
                    codeChallenge));

    assertThat(exception.getMessage(), is(equalTo("code_challenge_method must not be blank")));

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockValidator).isNotBlank(eq(null));
    verify(mockValidator, never()).isNotBlank(same(codeChallenge));
    verify(mockValidator, never()).isNotBlank(same(redirectUri));
  }

  @Test
  public void validate_CodeChallengeIsBlank_ExceptionThrown() {
    final var responseType = "code";
    final var state = "state";
    final var clientId = "the client id";
    final var codeChallengeMethod = "the method";
    final var codeChallenge = "";
    final var redirectUri = "the redirect URI";

    when(mockValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                subject.validate(
                    responseType,
                    state,
                    redirectUri,
                    clientId,
                    codeChallengeMethod,
                    codeChallenge));

    assertThat(exception.getMessage(), is(equalTo("code_challenge must not be blank")));

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockValidator).isNotBlank(same(codeChallengeMethod));
    verify(mockValidator).isNotBlank(eq(""));
    verify(mockValidator, never()).isNotBlank(same(redirectUri));
  }

  @Test
  public void validate_redirectUriIsBlank_ExceptionThrown() {
    final var responseType = "code";
    final var state = "state";
    final var clientId = "the client id";
    final var codeChallengeMethod = "the method";
    final var codeChallenge = "the code challenge";
    final var redirectUri = "";

    when(mockValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                subject.validate(
                    responseType,
                    state,
                    redirectUri,
                    clientId,
                    codeChallengeMethod,
                    codeChallenge));

    assertThat(exception.getMessage(), is(equalTo("redirect_uri must not be blank")));

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockValidator).isNotBlank(same(codeChallengeMethod));
    verify(mockValidator).isNotBlank(same(codeChallenge));
    verify(mockValidator).isNotBlank(eq(""));
  }
}
