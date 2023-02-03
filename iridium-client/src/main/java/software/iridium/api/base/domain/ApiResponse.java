/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.domain;

import java.io.Serializable;

public class ApiResponse implements Serializable {

  private static final long serialVersionUID = 4236626285723395336L;

  private String code;
  private String message;

  public ApiResponse() {
    super();
    this.code = "";
    this.message = "";
  }

  public ApiResponse(final String code, final String message) {
    this();
    this.setCode(code);
    this.setMessage(message);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String messages) {
    this.message = messages;
  }
}
