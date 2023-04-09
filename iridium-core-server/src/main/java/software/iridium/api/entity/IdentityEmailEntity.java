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

package software.iridium.api.entity;

import jakarta.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "identity_email_address_id"))
@Table(
    name = "identity_email_addresses",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"identity_id", "email_address"})})
public class IdentityEmailEntity extends AbstractEntity {

  private static final long serialVersionUID = 2816177551193171742L;

  @Column(name = "email_address", unique = false)
  private String emailAddress;

  @Column(name = "verified")
  private Boolean verified = false;

  @Column(name = "primary")
  private Boolean primary;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "identity_id")
  private IdentityEntity identity;

  @OneToOne(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "email",
      orphanRemoval = true)
  private EmailVerificationTokenEntity emailVerificationToken;

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Boolean isVerified() {
    return verified;
  }

  public Boolean isNotVerified() {
    return !verified;
  }

  public void setVerified(final Boolean verified) {
    this.verified = verified;
  }

  public Boolean getPrimary() {
    return primary;
  }

  public Boolean isPrimary() {
    return primary;
  }

  public void setPrimary(final Boolean primary) {
    this.primary = primary;
  }

  public IdentityEntity getIdentity() {
    return identity;
  }

  public void setIdentity(final IdentityEntity identity) {
    this.identity = identity;
  }

  public EmailVerificationTokenEntity getEmailVerificationToken() {
    return emailVerificationToken;
  }

  public void setEmailVerificationToken(final EmailVerificationTokenEntity emailVerificationToken) {
    this.emailVerificationToken = emailVerificationToken;
  }
}
