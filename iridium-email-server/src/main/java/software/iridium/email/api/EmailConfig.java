/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api;

import java.io.IOException;
import java.util.Properties;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Value("${spring.mail.properties.mail.transport.protocol}")
  private String protocol;

  @Value("${spring.mail.properties.mail.smtp.port}")
  private String port;

  @Value("${spring.mail.properties.mail.smtp.auth}")
  private String auth;

  @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
  private String TLSEnabled;

  @Value("${spring.mail.properties.mail.smtp.starttls.required}")
  private String TLSRequired;

  @Resource private ApplicationContext context;

  @Bean
  public JavaMailSender mailSender() throws IOException {
    final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    mailSender.setPort(Integer.parseInt(port));
    mailSender.setProtocol(protocol);
    mailSender.setUsername(username);
    mailSender.setPassword(password);
    Properties props = System.getProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.port", port);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.auth", "true");
    mailSender.setJavaMailProperties(props);
    return mailSender;
  }
}
