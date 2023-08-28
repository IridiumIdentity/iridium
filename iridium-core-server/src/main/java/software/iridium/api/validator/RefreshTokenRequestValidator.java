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
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@Component
public class RefreshTokenRequestValidator {
  @Autowired private AttributeValidator attributeValidator;

  public void validate(String grantType, String clientId, String refreshToken) {
    checkArgument(attributeValidator.isNotBlank(grantType), "grant_type must not be blank");

    checkArgument(attributeValidator.isNotBlank(clientId), "client_id must not be blank");

    checkArgument(attributeValidator.isNotBlank(refreshToken), "refresh_token must not be blank");

    checkArgument(
        attributeValidator.equals(
            grantType, AuthorizationCodeFlowConstants.REFRESH_TOKEN_GRANT_TYPE.getValue()),
        "grant_type value must be set to \"refresh_token\"");
  }
}
