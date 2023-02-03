/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import java.util.Map;
import org.springframework.stereotype.Component;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.entity.IdentityEmailEntity;

@Component
public class EmailSendRequestInstantiator {

  public EmailSendRequest instantiate(
      final IdentityEmailEntity email,
      final String subject,
      final Map<String, Object> props,
      final String template) {
    final var sendRequest = new EmailSendRequest();
    sendRequest.setTo(email.getEmailAddress());
    sendRequest.setSubject(subject);
    sendRequest.setProperties(props);
    sendRequest.setTemplate(template);

    return sendRequest;
  }
}
