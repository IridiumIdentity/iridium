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

import static com.google.common.base.Preconditions.checkArgument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.iridium.api.util.AttributeValidator;

@Component
public class AuthenticationRequestParamValidator {

  @Autowired private AttributeValidator attributeValidator;

  public void validate(
      final String responseType,
      final String state,
      final String redirectUri,
      final String clientId,
      final String codeChallengeMethod,
      final String codeChallenge) {
    checkArgument(attributeValidator.isNotBlank(clientId), "clientId must not be blank");
    checkArgument(
        attributeValidator.isNotBlank(codeChallengeMethod),
        "code_challenge_method must not be blank");
    checkArgument(attributeValidator.isNotBlank(codeChallenge), "code_challenge must not be blank");
    checkArgument(attributeValidator.isNotBlank(redirectUri), "redirect_uri must not be blank");
    checkArgument(attributeValidator.isNotBlank(state), "state must not be blank");
    checkArgument(attributeValidator.isNotBlank(responseType), "response_type must not be blank");
  }
}
