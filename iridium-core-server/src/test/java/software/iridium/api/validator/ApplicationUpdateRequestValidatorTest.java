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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.ApplicationUpdateRequest;
import software.iridium.api.util.AttributeValidator;

@ExtendWith(MockitoExtension.class)
class ApplicationUpdateRequestValidatorTest {

  @Mock private AttributeValidator mockValidator;
  @InjectMocks private ApplicationUpdateRequestValidator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockValidator);
  }

  @Test
  public void validate_AllGood_ValidatesAsExpected() {
    final var name = "the name";
    final var description = "the description";
    final var homepageUrl = "homepage URL";
    final var privacyPolicyUrl = "the policy url";
    final var redirectURI = "redirect Uri";
    final var iconUrl = "the icon url";
    final var applicationTypeId = "the app type id";
    final var request = new ApplicationUpdateRequest();
    request.setName(name);
    request.setDescription(description);
    request.setHomePageUrl(homepageUrl);
    request.setPrivacyPolicyUrl(privacyPolicyUrl);
    request.setRedirectUri(redirectURI);
    request.setIconUrl(iconUrl);
    request.setApplicationTypeId(applicationTypeId);

    when(mockValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenReturn(true);
    when(mockValidator.isValidUrl(anyString())).thenReturn(true);
    when(mockValidator.isNotBlank(anyString())).thenReturn(true);
    when(mockValidator.isUuid(same(applicationTypeId))).thenReturn(true);

    subject.validate(request);

    verify(mockValidator).isNotBlankAndNoLongerThan(same(name), eq(100));
    verify(mockValidator).isNotBlankAndNoLongerThan(same(description), eq(255));
    verify(mockValidator).isValidUrl(same(homepageUrl));
    verify(mockValidator).isValidUrl(same(privacyPolicyUrl));
    verify(mockValidator).isValidUrl(same(redirectURI));
    verify(mockValidator).isNotBlank(same(iconUrl));
    verify(mockValidator).isUuid(same(applicationTypeId));
  }
}
