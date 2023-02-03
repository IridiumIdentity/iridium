/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

public enum AccessTokenType {
  BEARER("Bearer");

  private String value;

  public String getValue() {
    return value;
  }

  AccessTokenType(final String value) {
    this.value = value;
  }
}
