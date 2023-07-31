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
import software.iridium.api.authentication.domain.ApplicationUpdateRequest;
import software.iridium.api.util.AttributeValidator;

@Component
public class ApplicationUpdateRequestValidator {

  @Autowired private AttributeValidator validator;

  public void validate(final ApplicationUpdateRequest request) {

    checkArgument(
        validator.isNotBlankAndNoLongerThan(request.getName(), 100),
        "name must not blank and no longer than 100 characters: " + request.getName());
    checkArgument(
        validator.ifPresentAndIsNotBlankAndNoLongerThan(request.getDescription(), 255),
        "description must not blank and no longer than 255 characters: "
            + request.getDescription());
    checkArgument(
        validator.isValidUrl(request.getHomePageUrl()),
        "homePageUrl must be a valid url: " + request.getHomePageUrl());
    if (validator.isNotBlank(request.getPrivacyPolicyUrl())) {
      checkArgument(
          validator.isValidUrl(request.getPrivacyPolicyUrl()),
          "privacyPolicyUrl must be a valid url: " + request.getPrivacyPolicyUrl());
    }

    checkArgument(
        validator.isValidUrl(request.getRedirectUri()),
        "redirectUri must be a valid url: " + request.getRedirectUri());

    checkArgument(
        validator.isUuid(request.getApplicationTypeId()),
        "applicationTypeId must not be a valid uuid: " + request.getApplicationTypeId());
  }
}
