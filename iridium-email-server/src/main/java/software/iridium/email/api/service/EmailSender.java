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
package software.iridium.email.api.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import software.iridium.api.email.domain.EmailAttachment;
import software.iridium.api.email.domain.EmailSendRequest;

@Service
public class EmailSender {

  @Resource private JavaMailSender mailSender;
  @Resource private FreeMarkerConfigurer freemarkerConfigurer;

  @Value("${spring.mail.fromAddress}")
  private String fromAddress;

  public void send(final EmailSendRequest request)
      throws MessagingException, IOException, TemplateException {
    MimeMessage message = mailSender.createMimeMessage();
    // todo (joshFischer correct multipart
    MimeMessageHelper helper =
        new MimeMessageHelper(
            message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
    for (EmailAttachment attachment : request.getAttachments()) {
      // todo  (josh fischer) get from s3
      helper.addAttachment(
          attachment.getAttachmentFileName(),
          new ClassPathResource("templates/missy-i-patio.jpeg"));
    }

    Template freemarkerTemplate =
        freemarkerConfigurer.getConfiguration().getTemplate(request.getTemplate() + ".ftl");
    String htmlBody =
        FreeMarkerTemplateUtils.processTemplateIntoString(
            freemarkerTemplate, request.getProperties());

    helper.setTo(request.getTo());
    helper.setText(htmlBody, true);
    helper.setSubject(request.getSubject());
    helper.setFrom(fromAddress);
    mailSender.send(message);
  }
}
