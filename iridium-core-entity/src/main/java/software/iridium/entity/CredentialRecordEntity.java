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
package software.iridium.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

// https://w3c.github.io/webauthn/#credential-record
@Entity
@AttributeOverride(name = "id", column = @Column(name = "credential_record_id"))
@Table(name = "credential_records")
public class CredentialRecordEntity extends AbstractEntity {
  @Serial private static final long serialVersionUID = -6144782531298542031L;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private CredentialType type;

  @Column(name = "raw_id")
  private String rawId;

  // The credential public key in authData.
  @Column(name = "public_key")
  private String publicKey;

  // authData.signCount
  @Column(name = "sign_count")
  private Integer signCount;

  // The value of the UV flag in authData.
  @Column(name = "user_verified_initialized")
  private Boolean userVerifiedInitialized;

  // The value returned from response.getTransports()., comma delimited
  @Column(name = "transports")
  private String transports;

  // The value of the BE flag when the public key credential source was created.
  @Column(name = "backup_eligible")
  private Boolean backupEligible;

  // The latest value of the BS flag in the authenticator data from any ceremony using the public
  // key credential source.
  @Column(name = "backup_state")
  private String backupState;

  // The value of the attestationObject attribute when the public key credential source was
  // registered.
  @Column(name = "attestation_object")
  private String attestationObject;

  // The value of the clientDataJSON attribute when the public key credential source was registered.
  @Column(name = "attestation_json")
  private String attestationJSON;

  public CredentialType getType() {
    return type;
  }

  public void setType(final CredentialType type) {
    this.type = type;
  }

  public String getRawId() {
    return rawId;
  }

  public void setRawId(final String rawId) {
    this.rawId = rawId;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(final String publicKey) {
    this.publicKey = publicKey;
  }

  public Integer getSignCount() {
    return signCount;
  }

  public void setSignCount(final Integer signCount) {
    this.signCount = signCount;
  }

  public Boolean getUserVerifiedInitialized() {
    return userVerifiedInitialized;
  }

  public void setUserVerifiedInitialized(final Boolean userVerifiedInitialized) {
    this.userVerifiedInitialized = userVerifiedInitialized;
  }

  public List<String> getTransports() {
    return Arrays.asList(transports.split(","));
  }

  public void setTransports(final List<String> transports) {
    this.transports = ArrayUtils.toString(transports);
  }

  public Boolean getBackupEligible() {
    return backupEligible;
  }

  public void setBackupEligible(final Boolean backupEligible) {
    this.backupEligible = backupEligible;
  }

  public String getBackupState() {
    return backupState;
  }

  public void setBackupState(final String backupState) {
    this.backupState = backupState;
  }

  public String getAttestationObject() {
    return attestationObject;
  }

  public void setAttestationObject(final String attestationObject) {
    this.attestationObject = attestationObject;
  }

  public String getAttestationJSON() {
    return attestationJSON;
  }

  public void setAttestationJSON(final String attestationJSON) {
    this.attestationJSON = attestationJSON;
  }
}
