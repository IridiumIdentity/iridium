/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
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
