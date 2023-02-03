/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.error;

public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = 3601878465892523213L;

  private static final String CODE = "403";
  private static final String MESSAGE = "FORBIDDEN";

  public AccessDeniedException() {
    super(MESSAGE);
  }

  public AccessDeniedException(final String message) {
    super(message);
  }

  public String getCode() {
    return CODE;
  }
}
