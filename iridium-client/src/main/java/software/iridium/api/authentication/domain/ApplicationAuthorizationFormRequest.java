/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationAuthorizationFormRequest implements Serializable {
  private static final long serialVersionUID = 2961573458519714392L;

  private String userToken;

  public String getUserToken() {
    return userToken;
  }

  public void setUserToken(final String userToken) {
    this.userToken = userToken;
  }
}
