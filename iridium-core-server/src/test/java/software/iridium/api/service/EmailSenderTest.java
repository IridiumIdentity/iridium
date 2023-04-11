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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.util.EmailTemplateProcessor;
import software.iridium.api.util.MimeMessageHelperInstantiator;

@ExtendWith(MockitoExtension.class)
class EmailSenderTest {

  @Mock private JavaMailSender mockMailSender;
  @Mock private FreeMarkerConfigurer mockFreemarkerConfigurer;

  @Mock private EmailTemplateProcessor mockTemplateProcessor;
  @Mock private MimeMessageHelperInstantiator mockHelperInstantiator;

  @Mock private MimeMessageHelper mockMimeMessageHelper;
  @Mock private MimeMessage mockMimeMessage;
  @InjectMocks private EmailSender subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockMailSender,
        mockFreemarkerConfigurer,
        mockTemplateProcessor,
        mockHelperInstantiator,
        mockMimeMessageHelper,
        mockMimeMessage);
  }

  @Test
  public void send_AllGood_BehavesAsExpected()
      throws MessagingException, TemplateException, IOException {
    final var to = "you@somewhere.com";
    final var template = "the template";
    final var text = "<div>the text</div>";
    final var emailSubject = "the subject";
    final var from = "me@somewherelse.com";
    final var request = new EmailSendRequest();
    request.setTo(to);
    request.setSubject(emailSubject);
    request.setTemplate(template);

    ReflectionTestUtils.setField(subject, "fromAddress", from);

    when(mockMailSender.createMimeMessage()).thenReturn(mockMimeMessage);
    when(mockHelperInstantiator.instantiate(same(mockMimeMessage)))
        .thenReturn(mockMimeMessageHelper);
    when(mockTemplateProcessor.processTemplateIntoString(same(request))).thenReturn(text);

    subject.send(request);

    verify(mockMailSender).createMimeMessage();
    verify(mockHelperInstantiator).instantiate(same(mockMimeMessage));
    verify(mockTemplateProcessor).processTemplateIntoString(same(request));
    verify(mockMimeMessageHelper).setTo(to);
    verify(mockMimeMessageHelper).setText(same(text), eq(true));
    verify(mockMimeMessageHelper).setSubject(same(emailSubject));
    verify(mockMimeMessageHelper).setFrom(same(from));
    verify(mockMailSender).send(same(mockMimeMessage));
  }
}
