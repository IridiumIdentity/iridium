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

public enum AuthorizationCodeFlowConstants {
  RESPONSE_TYPE("response_type"),
  CLIENT_ID("client_id"),
  CLIENT_SECRET("client_secret"),
  REDIRECT_URI("redirect_uri"),
  SCOPE("scope"),
  STATE("state"),
  CODE_CHALLENGE("code_challenge"),
  CODE_CHALLENGE_METHOD("code_challenge_method"),
  AUTHORIZATION_CODE("code"),
  AUTHORIZATION_CODE_GRANT_TYPE("authorization_code"),
  GRANT_TYPE("grant_type"),
  CODE_VERIFIER("code_verifier");

  private String value;

  public String getValue() {
    return value;
  }

  AuthorizationCodeFlowConstants(final String value) {
    this.value = value;
  }
}
