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
package software.iridium.api.handler;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.service.EmailService;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.IdentityEmailEntity;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.TenantEntity;

@ExtendWith(MockitoExtension.class)
class NewIdentityEventHandlerTest {

  @Mock private EmailService mockEmailService;
  @Mock private EmailSendRequestInstantiator mockEmailSendRequestInstantiator;

  @Mock private TenantEntityRepository mockTenantRepository;

  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @InjectMocks private NewIdentityEventHandler subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockEmailService,
        mockEmailSendRequestInstantiator,
        mockTenantRepository,
        mockApplicationRepository);
  }

  @Test
  public void handleEvent_AllGood_HandlesAsExpected() {
    final var primaryEmail = new IdentityEmailEntity();
    primaryEmail.setPrimary(true);
    final var identity = new IdentityEntity();
    identity.getEmails().add(primaryEmail);
    EmailSendRequest sendRequest = new EmailSendRequest();
    final var clientId = "theClientId";
    final var application = new ApplicationEntity();
    final var tenantId = "the tenant id";
    application.setTenantId(tenantId);
    final var tenant = new TenantEntity();

    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockEmailSendRequestInstantiator.instantiate(
            same(primaryEmail), eq("Iridium Email Verification"), anyMap(), eq("new-identity")))
        .thenReturn(sendRequest);
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));

    subject.handleEvent(identity, clientId);

    verify(mockEmailService).send(same(sendRequest));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockEmailSendRequestInstantiator)
        .instantiate(
            same(primaryEmail), eq("Iridium Email Verification"), anyMap(), eq("new-identity"));
    verify(mockTenantRepository).findById(same(tenantId));
  }
}
