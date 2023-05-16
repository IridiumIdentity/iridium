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
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.util.AttributeValidator;

@Component
public class SinglePageApplicationCreateRequestValidator {

  @Autowired private AttributeValidator attributeValidator;

  public void validate(final ApplicationCreateRequest request) {

    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getHomepageURL(), 100),
        "callback URL must not be blank and less than 100 characters");
    checkArgument(
        attributeValidator.isValidUrl(request.getCallbackURL()),
        "callback URL must be a valid URL");
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getHomepageURL(), 100),
        "homepage URL must not be blank and less than 100 characters");
    checkArgument(
        attributeValidator.isValidUrl(request.getHomepageURL()),
        "homepage URL must be a valid URL");

    if (attributeValidator.isNotBlank(request.getDescription())) {
      checkArgument(
          attributeValidator.isNotBlankAndNoLongerThan(request.getDescription(), 255),
          "description must not be more than 255 characters");
    }
  }
}
