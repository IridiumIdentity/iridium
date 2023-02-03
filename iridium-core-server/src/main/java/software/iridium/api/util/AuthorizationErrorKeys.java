/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.util;

public enum AuthorizationErrorKeys {
  INVALID_REQUEST("invalid_request"),
  ACCESS_DENIED("access_denied"),
  UNAUTHORIZED_CLIENT("unauthorized_client"),
  UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type"),
  INVALID_SCOPE("invalid_scope"),
  SERVER_ERROR("server_error"),
  TEMPORARILY_UNAVAILABLE("temporarily_unavailable");

  private String key;

  public String getKey() {
    return key;
  }

  AuthorizationErrorKeys(final String key) {
    this.key = key;
  }
}
