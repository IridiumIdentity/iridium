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
package software.iridium.api.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.email.domain.EmailSendRequest;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

  @Mock private EmailSender mockEmailSender;
  @InjectMocks private EmailService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEmailSender);
  }

  @Test
  public void send_AllGood_BehavesAsExpected()
      throws MessagingException, TemplateException, IOException {
    final var request = new EmailSendRequest();
    request.setTemplate("new-identity");

    subject.send(request);

    verify(mockEmailSender).send(same(request));
  }

  @Test
  public void send_SenderFailed_ExceptionThrown()
      throws MessagingException, TemplateException, IOException {
    final var request = new EmailSendRequest();
    request.setTemplate("new-identity");
    final var to = "you@somewhere.com";
    request.setTo(to);

    doThrow(new RuntimeException()).when(mockEmailSender).send(same(request));

    final var exception = assertThrows(RuntimeException.class, () -> subject.send(request));

    verify(mockEmailSender).send(same(request));

    assertThat(exception.getMessage(), is(equalTo("failed to send mail")));
  }
}
