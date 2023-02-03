/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "authentication_id"))
@Table(name = "authentications")
public class AuthenticationEntity implements Serializable {

  private static final long serialVersionUID = 2226019914052806053L;

  private static final String WEB_ID = "WEB_APPLICATION";

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", length = 36, nullable = false)
  private String id;

  @ManyToOne(
      cascade = {CascadeType.PERSIST},
      optional = false)
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
      createId = WEB_ID;
    }
  }

  public static String getWebId() {
    return WEB_ID;
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
