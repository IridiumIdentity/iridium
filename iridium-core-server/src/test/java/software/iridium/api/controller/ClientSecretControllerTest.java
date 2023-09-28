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

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.service.ClientSecretService;

@ExtendWith(MockitoExtension.class)
class ClientSecretControllerTest {

  @Mock private ClientSecretService mockClientSecretService;
  @Mock private HttpServletRequest mockServletRequest;
  @InjectMocks private ClientSecretController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockClientSecretService, mockServletRequest);
  }

  @Test
  public void create_AllGood_BehavesAsExpected() {
    final var applicationId = "app id";
    final var tenantId = "the tenant id";

    subject.create(mockServletRequest, tenantId, applicationId);

    verify(mockClientSecretService)
        .create(same(mockServletRequest), same(tenantId), same(applicationId));
  }

  @Test
  public void delete_AllGood_BehavesAsExpected() {
    final var applicationId = "the app id";
    final var clientSecretId = "the client secret";

    subject.delete(applicationId, clientSecretId);

    verify(mockClientSecretService).delete(same(applicationId), same(clientSecretId));
  }
}
