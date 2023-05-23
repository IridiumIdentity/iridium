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

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.iridium.api.base.error.BadRequestException;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.entity.ApplicationEntity;

@Component
public class ApplicationEntityAccessTokenRequestValidator {

  @Autowired private AttributeValidator attributeValidator;

  public void validate(final ApplicationEntity application, final Map<String, String> params) {

    if (attributeValidator.isNotBlank(
        params.getOrDefault(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), ""))) {
      if (attributeValidator.doesNotEqual(
          application.getRedirectUri(),
          params.get(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue()))) {
        throw new BadRequestException("redirect_uri is not valid");
      }
    }
  }
}
