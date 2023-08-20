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

import java.io.Serializable;

public class CreatePasskeyRequest implements Serializable {

  private static final long serialVersionUID = 3380797235216688784L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.authn.create-identity-request.1+json";

  private String handle;
  private String authenticatorAttachment;
  private String id;
  private byte[] rawId;
  private String type;
  private byte[] attestationObject;
  private byte[] clientDataJSON;
  private byte[] authenticatorData;
  private byte[] publicKey;
  private String algorithm;
  private String transports;

  public String getHandle() {
    return handle;
  }

  public void setHandle(final String handle) {
    this.handle = handle;
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

  public byte[] getAttestationObject() {
    return attestationObject;
  }

  public void setAttestationObject(final byte[] attestationObject) {
    this.attestationObject = attestationObject;
  }

  public byte[] getClientDataJSON() {
    return clientDataJSON;
  }

  public void setClientDataJSON(final byte[] clientDataJSON) {
    this.clientDataJSON = clientDataJSON;
  }

  public byte[] getAuthenticatorData() {
    return authenticatorData;
  }

  public void setAuthenticatorData(final byte[] authenticatorData) {
    this.authenticatorData = authenticatorData;
  }

  public byte[] getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(final byte[] publicKey) {
    this.publicKey = publicKey;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(final String algorithm) {
    this.algorithm = algorithm;
  }

  public String getTransports() {
    return transports;
  }

  public void setTransports(final String transports) {
    this.transports = transports;
  }
}
