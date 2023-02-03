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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.email.client.EmailApiClient;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.instantiator.SelfUrlGenerator;

@Component
public class PasswordEventHandler {

  @Resource private EmailSendRequestInstantiator emailInstantiator;
  @Resource private SelfUrlGenerator urlGenerator;
  @Resource private EmailApiClient emailApiClient;

  @Transactional(propagation = Propagation.REQUIRED)
  public void handlePasswordResetInitiatedEvent(
      final IdentityEntity identity, final String clientId) {
    // todo (josh fischer) implement
    Map<String, Object> props = new HashMap<>();
    props.put(
        "forgotPasswordLink",
        urlGenerator.generateChangePasswordUrl(identity.getPasswordResetToken(), clientId));

    // final var sendRequest = emailInstantiator.instantiate(identity, "Password Reset
    // Notification", props);
    // send send request to emai api
    // restTemplate.exchange()
  }

  public void handlePasswordResetEvent(final IdentityEntity identity) {
    final var primaryEmail = identity.getPrimaryEmail();
    if (primaryEmail != null) {
      Map<String, Object> props = new HashMap<>();
      props.put("tenantName", "IridiumID");
      props.put("tenantHelpEmail", "help@iridium.software");
      emailApiClient.sendNewIdentityVerificationMail(
          emailInstantiator.instantiate(
              primaryEmail,
              "Iridium Password Change Notification",
              props,
              "confirm-password-reset"));
    }
  }
}
