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
import software.iridium.api.email.client.EmailApiClient;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.instantiator.SelfUrlGenerator;

@ExtendWith(MockitoExtension.class)
class PasswordEventHandlerTest {

  @Mock private EmailSendRequestInstantiator mockEmailInstantiator;
  @Mock private SelfUrlGenerator mockUrlGenerator;
  @Mock private EmailApiClient mockEmailApiClient;
  @InjectMocks private PasswordEventHandler subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEmailApiClient, mockUrlGenerator, mockEmailInstantiator);
  }

  @Test
  public void handlePasswordResetEvent_AllGood_BehavesAsExpected() {
    final var identity = new IdentityEntity();
    final var email = new IdentityEmailEntity();
    email.setPrimary(true);
    identity.getEmails().add(email);
    final var sendRequest = new EmailSendRequest();

    when(mockEmailInstantiator.instantiate(
            same(email),
            eq("Iridium Password Change Notification"),
            anyMap(),
            eq("confirm-password-reset")))
        .thenReturn(sendRequest);

    subject.handlePasswordResetEvent(identity);

    verify(mockEmailInstantiator)
        .instantiate(
            same(email),
            eq("Iridium Password Change Notification"),
            anyMap(),
            eq("confirm-password-reset"));
    verify(mockEmailApiClient).sendNewIdentityVerificationMail(same(sendRequest));
  }
}
