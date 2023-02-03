/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

public enum Environment {
  SANDBOX("sandbox"),
  DEVELOPMENT("development"),
  STAGING("staging"),
  PRODUCTION("production");

  private String value;

  public String getValue() {
    return value;
  }

  Environment(final String value) {
    this.value = value;
  }
}
