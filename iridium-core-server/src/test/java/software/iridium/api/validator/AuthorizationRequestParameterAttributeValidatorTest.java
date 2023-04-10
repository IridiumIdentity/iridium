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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.generator.RedirectUrlGenerator;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@ExtendWith(MockitoExtension.class)
class AuthorizationRequestParameterAttributeValidatorTest {

  @Mock private RedirectUrlGenerator mockUrlGenerator;
  @Mock private AttributeValidator mockAttributeValidator;
  @InjectMocks private AuthorizationRequestParameterValidator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockUrlGenerator, mockAttributeValidator);
  }

  @Test
  public void validateAndOptionallyRedirect_AllGood_BehavesAsExpected() {
    final var redirectUri = "http://localhost:4200";
    final var state = "the state";
    final var responseType = "code";
    final var codeChallengeMethod = "S256";
    final var codeChallenge = "the code challenge";
    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), responseType);
    params.put(
        AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), codeChallengeMethod);
    params.put(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue(), codeChallenge);
    params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);

    when(mockAttributeValidator.isBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();

    assertTrue(subject.validateAndOptionallyRedirect(redirectUri, params).isEmpty());

    verify(mockAttributeValidator).isBlank(same(state));
    verify(mockAttributeValidator)
        .doesNotEqual(
            same(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()), same(responseType));
    verify(mockAttributeValidator)
        .doesNotEqual(same(CodeChallengeMethod.S256.getValue()), same(codeChallengeMethod));
    verify(mockAttributeValidator).isBlank(same(codeChallenge));
  }

  @Test
  public void validateAndOptionallyRedirect_PlainAllGood_BehavesAsExpected() {
    final var redirectUri = "http://localhost:4200";
    final var state = "the state";
    final var responseType = "code";
    final var codeChallengeMethod = "plain";
    final var codeChallenge = "the code challenge";
    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), responseType);
    params.put(
        AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), codeChallengeMethod);
    params.put(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue(), codeChallenge);
    params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);

    when(mockAttributeValidator.isBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();

    assertTrue(subject.validateAndOptionallyRedirect(redirectUri, params).isEmpty());

    verify(mockAttributeValidator).isBlank(same(state));
    verify(mockAttributeValidator)
        .doesNotEqual(
            same(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()), same(responseType));
    verify(mockAttributeValidator)
        .doesNotEqual(same(CodeChallengeMethod.S256.getValue()), same(codeChallengeMethod));
    verify(mockAttributeValidator)
        .doesNotEqual(same(CodeChallengeMethod.PLAIN.getValue()), same(codeChallengeMethod));
    verify(mockAttributeValidator).isBlank(same(codeChallenge));
  }

  @Test
  public void validateAndOptionallyRedirect_StateIsBlank_RedirectsInvalidRequest() {
    final var redirectUri = "http://localhost:4200";
    final var state = "";
    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);
    final var errorRedirectUrl = "http://localhost:4200?invalid_request=state parameter is blank";

    when(mockAttributeValidator.isBlank(anyString())).thenCallRealMethod();
    when(mockUrlGenerator.generate(same(redirectUri), any())).thenCallRealMethod();

    final var response = subject.validateAndOptionallyRedirect(redirectUri, params);

    assertThat(response, is(equalTo(errorRedirectUrl)));
    verify(mockAttributeValidator).isBlank(same(state));
    verify(mockUrlGenerator).generate(same(redirectUri), any());
  }

  @Test
  public void
      validateAndOptionallyRedirect_ResponseTypeIsNotAuthorizationCode_RedirectsInvalidRequest() {
    final var redirectUri = "http://localhost:4200";
    final var state = "the state";
    final var responseType = "something else";
    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), responseType);
    params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);
    final var errorRedirectUrl =
        "http://localhost:4200?unsupported_response_type=authorization server does not support"
            + " response type: "
            + responseType;

    when(mockAttributeValidator.isBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();
    when(mockUrlGenerator.generate(same(redirectUri), any())).thenCallRealMethod();

    final var response = subject.validateAndOptionallyRedirect(redirectUri, params);

    verify(mockAttributeValidator).isBlank(same(state));
    verify(mockAttributeValidator)
        .doesNotEqual(
            same(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()), same(responseType));
    verify(mockUrlGenerator).generate(same(redirectUri), any());
    assertThat(response, is(equalTo(errorRedirectUrl)));
  }

  @Test
  public void validateAndOptionallyRedirect_CodeChallengeMethodNotS256_RedirectsInvalidRequest() {
    final var redirectUri = "http://localhost:4200";
    final var state = "the state";
    final var responseType = "code";
    final var codeChallengeMethod = "S2561";
    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), responseType);
    params.put(
        AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), codeChallengeMethod);
    params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);

    final var errorRedirectUrl =
        "http://localhost:4200?invalid_request=code_challenge_method not supported: "
            + codeChallengeMethod;

    when(mockAttributeValidator.isBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();
    when(mockUrlGenerator.generate(same(redirectUri), any())).thenCallRealMethod();

    final var response = subject.validateAndOptionallyRedirect(redirectUri, params);

    verify(mockAttributeValidator).isBlank(same(state));
    verify(mockAttributeValidator)
        .doesNotEqual(
            same(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()), same(responseType));
    verify(mockAttributeValidator)
        .doesNotEqual(same(CodeChallengeMethod.S256.getValue()), same(codeChallengeMethod));

    verify(mockUrlGenerator).generate(same(redirectUri), any());
    assertThat(response, is(equalTo(errorRedirectUrl)));
  }

  @Test
  public void
      validateAndOptionallyRedirect_CodeChallengeMethodNotS256OrPlain_RedirectsInvalidRequest() {
    final var redirectUri = "http://localhost:4200";
    final var state = "the state";
    final var responseType = "code";
    final var codeChallengeMethod = "plains";
    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), responseType);
    params.put(
        AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), codeChallengeMethod);
    params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);

    final var errorRedirectUrl =
        "http://localhost:4200?invalid_request=code_challenge_method not supported: "
            + codeChallengeMethod;

    when(mockAttributeValidator.isBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();
    when(mockUrlGenerator.generate(same(redirectUri), any())).thenCallRealMethod();

    final var response = subject.validateAndOptionallyRedirect(redirectUri, params);

    verify(mockAttributeValidator).isBlank(same(state));
    verify(mockAttributeValidator)
        .doesNotEqual(
            same(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()), same(responseType));
    verify(mockAttributeValidator)
        .doesNotEqual(same(CodeChallengeMethod.S256.getValue()), same(codeChallengeMethod));
    verify(mockAttributeValidator)
        .doesNotEqual(same(CodeChallengeMethod.PLAIN.getValue()), same(codeChallengeMethod));

    verify(mockUrlGenerator).generate(same(redirectUri), any());
    assertThat(response, is(equalTo(errorRedirectUrl)));
  }

  @Test
  public void validateAndOptionallyRedirect_CodeChallengeIsBlank_RedirectsInvalidRequest() {
    final var redirectUri = "http://localhost:4200";
    final var state = "the state";
    final var responseType = "code";
    final var codeChallengeMethod = "S256";
    final var codeChallenge = "";
    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), responseType);
    params.put(
        AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), codeChallengeMethod);
    params.put(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue(), codeChallenge);

    params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);

    final var errorRedirectUrl =
        "http://localhost:4200?invalid_request=code_challenge must not be blank";

    when(mockAttributeValidator.isBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();
    when(mockUrlGenerator.generate(same(redirectUri), any())).thenCallRealMethod();

    final var response = subject.validateAndOptionallyRedirect(redirectUri, params);

    verify(mockAttributeValidator).isBlank(same(state));
    verify(mockAttributeValidator)
        .doesNotEqual(
            same(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()), same(responseType));
    verify(mockAttributeValidator)
        .doesNotEqual(same(CodeChallengeMethod.S256.getValue()), same(codeChallengeMethod));
    verify(mockAttributeValidator).isBlank(same(codeChallenge));

    verify(mockUrlGenerator).generate(same(redirectUri), any());
    assertThat(response, is(equalTo(errorRedirectUrl)));
  }
}
