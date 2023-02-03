/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.error;

public class ClientCallException extends RuntimeException {

  private static final long serialVersionUID = -1385373312223048976L;

  public ClientCallException(final String message) {
    super(message);
  }

  public ClientCallException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
