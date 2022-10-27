/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.handler;

import software.iridium.api.email.client.EmailApiClient;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.entity.IdentityEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class NewIdentityEventHandler {

    @Resource
    private EmailApiClient emailApiClient;
    @Resource
    private EmailSendRequestInstantiator emailSendRequestInstantiator;
    @Value("${software.iridium.emailNotification.client.baseUrl}")
    private String verifyEmailLink;
    //todo: test me
    public void handleEvent(final IdentityEntity identity, final String clientId) {
        Map<String, Object> props = new HashMap<>();
        final var primaryEmail = identity.getPrimaryEmail();
        props.put("verifyEmailLink", verifyEmailLink + "login?id=" + primaryEmail.getEmailAddress() + "&client_id=" + clientId);
        emailApiClient.sendNewIdentityVerificationMail(
                emailSendRequestInstantiator.instantiate(
                        primaryEmail,
                        "Iridium Email Verification",
                        props,
                        "new-identity"
                )
        );
    }
}
