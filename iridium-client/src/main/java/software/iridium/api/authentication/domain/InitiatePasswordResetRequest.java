/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class InitiatePasswordResetRequest implements Serializable {

  private static final long serialVersionUID = -5866090708514610764L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.initiate-forgot-password-request.1+json";

  private String username;

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
}
