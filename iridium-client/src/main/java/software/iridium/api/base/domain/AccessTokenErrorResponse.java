/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.domain;

import java.io.Serializable;

public class AccessTokenErrorResponse implements Serializable {

  private static final long serialVersionUID = 6609889412637582655L;

  private String error;

  public AccessTokenErrorResponse() {
    super();
    this.error = "";
  }

  public AccessTokenErrorResponse(final String error) {
    this();
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String messages) {
    this.error = messages;
  }
}
