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

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.util.EmailTemplateProcessor;
import software.iridium.api.util.MimeMessageHelperInstantiator;

@Service
public class EmailSender {

  @Resource private JavaMailSender mailSender;

  @Resource private EmailTemplateProcessor templateProcessor;

  @Resource private MimeMessageHelperInstantiator messageHelperInstantiator;

  @Value("${spring.mail.fromAddress}")
  private String fromAddress;

  public void send(final EmailSendRequest request)
      throws MessagingException, IOException, TemplateException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = messageHelperInstantiator.instantiate(message);

    String htmlBody = templateProcessor.processTemplateIntoString(request);

    helper.setTo(request.getTo());
    helper.setText(htmlBody, true);
    helper.setSubject(request.getSubject());
    helper.setFrom(fromAddress);
    mailSender.send(message);
  }
}
