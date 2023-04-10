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

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@Component
public class AuthenticationRequestParamValidator {

  @Resource private AttributeValidator attributeValidator;

  public void validate(final Map<String, String> params) {
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), "")),
        "clientId must not be blank");
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(
                AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), "")),
        "code_challenge_method must not be blank");
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue(), "")),
        "code_challenge must not be blank");
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), "")),
        "redirect_uri must not be blank");
  }
}
