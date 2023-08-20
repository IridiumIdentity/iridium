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
@AttributeOverride(name = "id", column = @Column(name = "challenge_id"))
@Table(name = "challenges")
public class ChallengeEntity extends AbstractEntity {
  @Serial private static final long serialVersionUID = -8283079429392253794L;

  @Column(name = "challenge", updatable = false, nullable = false)
  private String challenge;

  @Column(name = "origin", updatable = false, nullable = false)
  private String origin;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tenant_id")
  private TenantEntity tenant;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "expiration", nullable = false)
  private Date expiration;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private ChallengeType type;

  public String getChallenge() {
    return challenge;
  }

  public void setChallenge(final String challenge) {
    this.challenge = challenge;
  }

  public TenantEntity getTenant() {
    return tenant;
  }

  public void setTenant(final TenantEntity tenant) {
    this.tenant = tenant;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(final Date expiration) {
    this.expiration = expiration;
  }

  public ChallengeType getType() {
    return type;
  }

  public void setType(final ChallengeType type) {
    this.type = type;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(final String origin) {
    this.origin = origin;
  }
}
