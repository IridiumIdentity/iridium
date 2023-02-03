/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.handler;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.iridium.api.email.client.EmailApiClient;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;

@Component
public class NewIdentityEventHandler {

  @Resource private EmailApiClient emailApiClient;
  @Resource private EmailSendRequestInstantiator emailSendRequestInstantiator;

  @Value("${software.iridium.emailNotification.client.baseUrl}")
  private String verifyEmailLink;
  // todo: test me
  public void handleEvent(final IdentityEntity identity, final String clientId) {
    Map<String, Object> props = new HashMap<>();
    final var primaryEmail = identity.getPrimaryEmail();
    props.put(
        "verifyEmailLink",
        verifyEmailLink + "login?id=" + primaryEmail.getEmailAddress() + "&client_id=" + clientId);
    emailApiClient.sendNewIdentityVerificationMail(
        emailSendRequestInstantiator.instantiate(
            primaryEmail, "Iridium Email Verification", props, "new-identity"));
  }
}
