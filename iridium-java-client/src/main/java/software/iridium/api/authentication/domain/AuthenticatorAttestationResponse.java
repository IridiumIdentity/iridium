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

public class AuthenticatorAttestationResponse implements Serializable {
  @Serial private static final long serialVersionUID = -148827600071978916L;

  private byte[] attestationObject;

  private byte[] clientDataJson;

  public byte[] getAttestationObject() {
    return attestationObject;
  }

  public void setAttestationObject(final byte[] attestationObject) {
    this.attestationObject = attestationObject;
  }

  public byte[] getClientDataJson() {
    return clientDataJson;
  }

  public void setClientDataJson(final byte[] clientDataJson) {
    this.clientDataJson = clientDataJson;
  }
}
