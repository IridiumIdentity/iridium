/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "password_reset_token_id"))
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = 172400918530567499L;

  @Column(name = "token")
  private String token;

  @OneToOne(optional = false)
  @JoinColumn(name = "identity_id")
  private IdentityEntity identity;

  @Column(name = "EXPIRATION")
  @Temporal(TemporalType.TIMESTAMP)
  private Date expiration;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }

  public IdentityEntity getIdentity() {
    return identity;
  }

  public void setIdentity(final IdentityEntity identity) {
    this.identity = identity;
  }
}
