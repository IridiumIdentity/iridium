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
package software.iridium.api.authentication.domain;

import java.io.Serial;
import java.io.Serializable;

public class AuthenticationRequest implements Serializable {
  @Serial private static final long serialVersionUID = 1647895142648119054L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.authentication-request.1+json";

  private String username;
  private String password;
  private String clientId;
  private String authenticatorAttachment;
  private String id;
  private byte[] rawId;

  private String type;

  private AuthenticatorAttestationResponse response;

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public String getAuthenticatorAttachment() {
    return authenticatorAttachment;
  }

  public void setAuthenticatorAttachment(final String authenticatorAttachment) {
    this.authenticatorAttachment = authenticatorAttachment;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public byte[] getRawId() {
    return rawId;
  }

  public void setRawId(final byte[] rawId) {
    this.rawId = rawId;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public AuthenticatorAttestationResponse getResponse() {
    return response;
  }

  public void setResponse(final AuthenticatorAttestationResponse response) {
    this.response = response;
  }
}
