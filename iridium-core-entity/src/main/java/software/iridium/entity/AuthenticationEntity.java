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
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "authentication_id"))
@Table(name = "authentications")
public class AuthenticationEntity implements Serializable {

  private static final long serialVersionUID = 2226019914052806053L;

  private static final String SERVER_APP_ID = "SERVER_APP";

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", length = 36, nullable = false)
  private String id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "identity_id")
  private IdentityEntity identity;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", nullable = false)
  private Date created;

  @Column(name = "create_id", length = 128, nullable = false)
  private String createId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "expiration", nullable = false)
  private Date expiration;

  @Column(name = "auth_token", length = 255, nullable = false)
  private String authToken;

  @Column(name = "refresh_token", length = 255, nullable = false)
  private String refreshToken;

  @PrePersist
  public void prePersist() {
    if (created == null) {
      created = new Date();
    }
    if (StringUtils.isBlank(createId)) {
      createId = SERVER_APP_ID;
    }
  }

  public static String getWebId() {
    return SERVER_APP_ID;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public IdentityEntity getIdentity() {
    return identity;
  }

  public void setIdentity(final IdentityEntity identity) {
    this.identity = identity;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(final Date created) {
    this.created = created;
  }

  public String getCreateId() {
    return createId;
  }

  public void setCreateId(final String createId) {
    this.createId = createId;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(final Date expiration) {
    this.expiration = expiration;
  }

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(final String authToken) {
    this.authToken = authToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(final String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
