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

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.util.AttributeValidator;

@Component
public class AuthenticationRequestValidator {

  @Resource private AttributeValidator attributeValidator;

  public void validate(final AuthenticationRequest request) {
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getUsername(), 100),
        "username must not blank and no longer than 100 characters: " + request.getUsername());
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getPassword(), 100),
        "password must not blank and no longer than 100 characters");
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getClientId(), 32),
        "clientId must not be blank and no longer than 32 characters");
  }
}
