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
package software.iridium.api.controller;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.service.ExternalIdentityProviderTemplateService;

@ExtendWith(MockitoExtension.class)
class ExternalIdentityProviderTemplateControllerTest {

  @Mock private ExternalIdentityProviderTemplateService mockProviderService;
  @InjectMocks private ExternalIdentityProviderTemplateController subject;

  @AfterEach
  public void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(mockProviderService);
  }

  @Test
  public void retrieveAllSummaries_AllGood_BehavesAsExpected() {

    subject.retrieveAllSummaries();

    verify(mockProviderService).retrieveAllSummaries();
  }

  @Test
  public void retrieveAvailableSummaries_AllGood_BehavesAsExpected() {
    final var tenantId = "the tenant id";

    subject.retrieveAvailableSummaries(tenantId);

    verify(mockProviderService).retrieveAvailableSummaries(tenantId);
  }
}
