/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.entity.PasswordResetTokenEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SelfUrlGenerator {

    @Value("${software.iridium.passwordReset.client.baseUrl}")
    private String baseUrl;

    public String generateChangePasswordUrl(final PasswordResetTokenEntity passwordResetToken, final String clientId) {
        return baseUrl
                + "reset-password?token="
                + passwordResetToken.getToken()
                + "&client_id="
                + clientId;
    }
}
