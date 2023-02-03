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
@AttributeOverride(name = "id", column = @Column(name = "email_verification_token_id"))
@Table(name = "email_verification_tokens")
public class EmailVerificationTokenEntity extends UuidIdentifiableAndAuditable {

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
