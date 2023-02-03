/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.entity;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import software.iridium.api.entity.UuidIdentifiableAndAuditable;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "email_verification_token_id"))
@Table(name = "email_verification_tokens")
public class EmailVerificationTokenEntity extends UuidIdentifiableAndAuditable {

  @Column(name = "token")
  private String token;

  @Column(name = "identity_email_id", nullable = false, length = 36)
  private String userEmail;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "EXPIRATION", nullable = false, updatable = false)
  private Date expiration;

  public String getToken() {
    return token;
  }

  public void setToken(final String token) {
    this.token = token;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(final String userEmail) {
    this.userEmail = userEmail;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(final Date expiration) {
    this.expiration = expiration;
  }
}
