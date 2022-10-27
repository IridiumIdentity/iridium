/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class PasswordResetRequest implements Serializable {

    public static final String MEDIA_TYPE = "application/vnd.iridium.id.password-reset-request.1+json";

    private static final long serialVersionUID = 4974051546194451918L;

    private String token;

    private String newPassword;

    private String clientId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }
}
