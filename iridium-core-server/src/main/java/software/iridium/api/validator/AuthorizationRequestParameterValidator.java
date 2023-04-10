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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.generator.RedirectUrlGenerator;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.AuthorizationErrorKeys;

@Component
public class AuthorizationRequestParameterValidator {

  @Resource private RedirectUrlGenerator redirectUrlGenerator;
  @Resource private AttributeValidator attributeValidator;

  public String validateAndOptionallyRedirect(
      final String redirectUri, final Map<String, String> params) {
    final var containerErrorMap = new HashMap<String, MultiValueMap<String, String>>();
    // todo: place these validattions where appropriate, if appropriate
    if (attributeValidator.isBlank(
        params.getOrDefault(AuthorizationCodeFlowConstants.STATE.getValue(), ""))) {
      final var detailErrorMap = new LinkedMultiValueMap<String, String>();
      detailErrorMap.put(
          AuthorizationErrorKeys.INVALID_REQUEST.getKey(), List.of("state parameter is blank"));
      containerErrorMap.put("error", detailErrorMap);
      return redirectUrlGenerator.generate(redirectUri, detailErrorMap);
    }

    if (attributeValidator.doesNotEqual(
        AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue(),
        params.get(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue()))) {
      final var detailErrorMap = new LinkedMultiValueMap<String, String>();
      detailErrorMap.put(
          AuthorizationErrorKeys.UNSUPPORTED_RESPONSE_TYPE.getKey(),
          List.of(
              "authorization server does not support response type: "
                  + params.get(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue())));
      containerErrorMap.put("error", detailErrorMap);
      return redirectUrlGenerator.generate(redirectUri, detailErrorMap);
    }

    final var codeChallengeMethod =
        params.get(AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue());

    // todo validate pcke code verification
    if (attributeValidator.doesNotEqual(CodeChallengeMethod.S256.getValue(), codeChallengeMethod)
        && attributeValidator.doesNotEqual(
            CodeChallengeMethod.PLAIN.getValue(), codeChallengeMethod)) {
      final var detailErrorMap = new LinkedMultiValueMap<String, String>();
      detailErrorMap.put(
          AuthorizationErrorKeys.INVALID_REQUEST.getKey(),
          List.of(
              "code_challenge_method not supported: "
                  + params.get(AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue())));
      containerErrorMap.put("error", detailErrorMap);
      return redirectUrlGenerator.generate(redirectUri, detailErrorMap);
    }

    if (attributeValidator.isBlank(
        params.get(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue()))) {
      final var detailErrorMap = new LinkedMultiValueMap<String, String>();
      detailErrorMap.put(
          AuthorizationErrorKeys.INVALID_REQUEST.getKey(),
          List.of("code_challenge must not be blank"));
      containerErrorMap.put("error", detailErrorMap);
      return redirectUrlGenerator.generate(redirectUri, detailErrorMap);
    }
    return "";
  }
}
