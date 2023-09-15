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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "identity_email_address_id"))
@Table(
    name = "identity_email_addresses",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"identity_id", "email_address"})})
public class IdentityEmailEntity extends AbstractEntity {

  private static final long serialVersionUID = 2816177551193171742L;

  @Column(name = "email_address", unique = false)
  private String emailAddress;

  @Column(name = "is_verified")
  @Getter(AccessLevel.NONE)
  private Boolean verified = false;

  @Column(name = "is_primary")
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

  public Boolean isPrimary() {
    return primary;
  }

  public Boolean isVerified() {
    return verified;
  }

  public Boolean isNotVerified() {
    return !verified;
  }

  /**
   * public String getEmailAddress() { return emailAddress; }
   *
   * <p>public void setEmailAddress(final String emailAddress) { this.emailAddress = emailAddress; }
   *
   * <p>public void setVerified(final Boolean verified) { this.verified = verified; }
   *
   * <p>public Boolean getPrimary() { return primary; }
   *
   * <p>public void setPrimary(final Boolean primary) { this.primary = primary; }
   *
   * <p>public IdentityEntity getIdentity() { return identity; }
   *
   * <p>public void setIdentity(final IdentityEntity identity) { this.identity = identity; }
   *
   * <p>public EmailVerificationTokenEntity getEmailVerificationToken() { return
   * emailVerificationToken; }
   *
   * <p>public void setEmailVerificationToken(final EmailVerificationTokenEntity
   * emailVerificationToken) { this.emailVerificationToken = emailVerificationToken; }
   */
}
