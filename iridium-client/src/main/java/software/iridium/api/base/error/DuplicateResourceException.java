/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.error;

public class DuplicateResourceException extends RuntimeException {

  private static final long serialVersionUID = 2114443363758574910L;

  private static final String CODE = "409";
  private static final String MESSAGE = "CONFLICT";

  public DuplicateResourceException() {
    super(MESSAGE);
  }

  public DuplicateResourceException(final String message) {
    super(message);
  }

  public String getCode() {
    return CODE;
  }
}
