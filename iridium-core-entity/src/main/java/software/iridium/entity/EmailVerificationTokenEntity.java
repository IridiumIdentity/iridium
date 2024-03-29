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
import java.util.Date;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "email_verification_token_id"))
@Table(name = "email_verification_tokens")
public class EmailVerificationTokenEntity extends AbstractEntity {

  @Serial private static final long serialVersionUID = 2211848227772877113L;

  @Column(name = "token")
  private String token;

  @OneToOne
  @JoinColumn(name = "identity_email_id")
  private IdentityEmailEntity email;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRATION", nullable = false, updatable = false)
  private Date expiration;

  public String getToken() {
    return token;
  }

  public void setToken(final String token) {
    this.token = token;
  }

  public IdentityEmailEntity getEmail() {
    return email;
  }

  public void setEmail(final IdentityEmailEntity email) {
    this.email = email;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(final Date expiration) {
    this.expiration = expiration;
  }
}
