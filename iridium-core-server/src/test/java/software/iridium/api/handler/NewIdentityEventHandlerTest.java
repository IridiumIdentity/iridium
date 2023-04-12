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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.service.EmailService;

@ExtendWith(MockitoExtension.class)
class NewIdentityEventHandlerTest {

  @Mock private EmailService mockEmailService;
  @Mock private EmailSendRequestInstantiator mockEmailSendRequestInstantiator;
  @InjectMocks private NewIdentityEventHandler subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEmailService, mockEmailSendRequestInstantiator);
  }

  @Test
  public void handleEvent_AllGood_HandlesAsExpected() {
    final var tempPassword = "theTempPassword";
    final var primaryEmail = new IdentityEmailEntity();
    primaryEmail.setPrimary(true);
    final var identity = new IdentityEntity();
    identity.getEmails().add(primaryEmail);
    EmailSendRequest sendRequest = new EmailSendRequest();
    final var clientId = "theClientId";

    when(mockEmailSendRequestInstantiator.instantiate(
            same(primaryEmail), eq("Iridium Email Verification"), anyMap(), eq("new-identity")))
        .thenReturn(sendRequest);

    subject.handleEvent(identity, clientId);

    verify(mockEmailService).send(same(sendRequest));
    verify(mockEmailSendRequestInstantiator)
        .instantiate(
            same(primaryEmail), eq("Iridium Email Verification"), anyMap(), eq("new-identity"));
  }
}
