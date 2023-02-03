/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import java.util.Date;
import javax.persistence.*;

@Entity
@AttributeOverride(
    name = "id",
    column = @Column(name = "in_progress_external_identity_provider_authorization_id"))
@Table(name = "in_progress_external_identity_provider_authorizations")
public class InProgressExternalIdentityProviderAuthorizationEntity
    extends UuidIdentifiableAndAuditable {

  @Column(name = "state", length = 100, nullable = false)
  private String state;

  @Column(name = "redirect_uri", length = 255, nullable = false)
  private String redirectUri;

  @Column(name = "clientId", length = 42, nullable = false)
  private String clientId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "expiration", nullable = false)
  private Date expiration;

  @ManyToOne
  @JoinColumn(name = "external_identity_provider_id")
  private ExternalIdentityProviderEntity provider;

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(final String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(final Date expiration) {
    this.expiration = expiration;
  }

  public ExternalIdentityProviderEntity getProvider() {
    return provider;
  }

  public void setProvider(final ExternalIdentityProviderEntity provider) {
    this.provider = provider;
  }
}
