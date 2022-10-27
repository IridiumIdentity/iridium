/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class CreateIdentityRequest implements Serializable {

    private static final long serialVersionUID = 3380797235216688784L;

    public static final String MEDIA_TYPE = "application/vnd.catalyst11.authn.create-identity-request.1+json";

    private String username;

    private String password;

    private String clientId;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
