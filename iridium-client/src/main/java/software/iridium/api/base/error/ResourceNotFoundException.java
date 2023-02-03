/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.base.error;

public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 7180682033933421062L;

  private static final String CODE = "404";

  public ResourceNotFoundException(final String message) {
    super(message);
  }

  public String getCode() {
    return CODE;
  }
}
