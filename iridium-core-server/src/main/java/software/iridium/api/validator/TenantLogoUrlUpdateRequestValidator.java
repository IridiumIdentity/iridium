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
import software.iridium.api.authentication.domain.TenantLogoUrlUpdateRequest;
import software.iridium.api.util.AttributeValidator;

@Component
public class TenantLogoUrlUpdateRequestValidator {

  private static final int MAX_LENGTH = 1024;

  @Autowired private AttributeValidator validator;

  public void validate(final TenantLogoUrlUpdateRequest request) {
    checkArgument(
        validator.isNotBlankAndNoLongerThan(request.getLogoUrl(), MAX_LENGTH),
        "logoUrl must not be blank and no longer than " + MAX_LENGTH + " characters");
    checkArgument(validator.isValidUrl(request.getLogoUrl()), "logoUrl must be a valid url");
  }
}
