/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.error;

public class NotAuthorizedException extends RuntimeException {

  private static final long serialVersionUID = -6642306545010507296L;

  private static final String CODE = "401";
  private static final String MESSAGE = "NOT AUTHORIZED";

  public NotAuthorizedException() {
    super(MESSAGE);
  }

  public NotAuthorizedException(final String message) {
    super(message);
  }

  public String getCode() {
    return CODE;
  }
}
