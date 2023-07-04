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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderRequest;
import software.iridium.api.util.AttributeValidator;

@ExtendWith(MockitoExtension.class)
class CreateExternalIdentityProviderRequestValidatorTest {

  @Mock private AttributeValidator mockValidator;
  @InjectMocks private CreateExternalIdentityProviderRequestValidator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockValidator);
  }

  @Test
  public void validate_AllGood_BehavesAsExpected() {
    final var templateId = "some id";
    final var clientId = "the client id";
    final var clientSecret = "the client secret";
    final var request = new CreateExternalIdentityProviderRequest();
    request.setExternalProviderTemplateId(templateId);
    request.setClientId(clientId);
    request.setClientSecret(clientSecret);
    when(mockValidator.isUuid(anyString())).thenReturn(true);
    when(mockValidator.isNotBlank(anyString())).thenReturn(true);

    subject.validate(request);

    verify(mockValidator).isUuid(same(templateId));
    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockValidator).isNotBlank(same(clientSecret));
  }
}
