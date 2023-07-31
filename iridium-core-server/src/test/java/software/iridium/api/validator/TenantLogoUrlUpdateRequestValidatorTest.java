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
import software.iridium.api.authentication.domain.TenantLogoUrlUpdateRequest;
import software.iridium.api.util.AttributeValidator;

@ExtendWith(MockitoExtension.class)
class TenantLogoUrlUpdateRequestValidatorTest {

  @Mock private AttributeValidator mockValidator;
  @InjectMocks private TenantLogoUrlUpdateRequestValidator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockValidator);
  }

  @Test
  public void validate_AllGood_BehavesAsExpected() {
    final var logoUrl = "the url";
    final var request = new TenantLogoUrlUpdateRequest();
    request.setLogoUrl(logoUrl);

    when(mockValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();
    when(mockValidator.isValidUrl(same(logoUrl))).thenReturn(true);

    subject.validate(request);

    verify(mockValidator).isNotBlankAndNoLongerThan(same(logoUrl), eq(1024));
    verify(mockValidator).isValidUrl(same(logoUrl));
  }
}
