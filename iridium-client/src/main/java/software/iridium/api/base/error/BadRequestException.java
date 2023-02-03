/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.error;

public class BadRequestException extends RuntimeException {

  private static final long serialVersionUID = 4441204485680238874L;

  private static final String CODE = "400";
  private static final String MESSAGE = "BAD REQUEST";

  public BadRequestException(final String message) {
    super(message);
  }

  public BadRequestException() {
    super(MESSAGE);
  }

  public String getCode() {
    return CODE;
  }
}
