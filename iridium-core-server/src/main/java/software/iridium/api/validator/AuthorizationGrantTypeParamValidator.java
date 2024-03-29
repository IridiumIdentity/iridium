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
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@Component
public class AuthorizationGrantTypeParamValidator {

  @Autowired private AttributeValidator attributeValidator;

  public void validate(final Map<String, String> params) {
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), "")),
        "response_type must not be blank");
    if (Objects.equals(
        params.get(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue()),
        AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue())) {
      checkArgument(
          attributeValidator.isNotBlank(
              params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), "")),
          "client_id must not be blank");
    }
  }
}
