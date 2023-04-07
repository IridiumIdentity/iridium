/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
