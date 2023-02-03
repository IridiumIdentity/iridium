/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "external_identity_provider_template_id"))
@Table(name = "external_identity_provider_templates")
public class ExternalIdentityProviderTemplateEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = -812177502861094777L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "client_id", length = 100, nullable = false)
  private String clientId;

  @Column(name = "client_secret", length = 100, nullable = true)
  private String clientSecret;

  @Column(name = "identity_request_base_url", length = 255, nullable = false)
  private String identityRequestBaseUrl;

  @Column(name = "profile_request_base_url", length = 255, nullable = false)
  private String profileRequestBaseUrl;

  @Column(name = "icon_path", length = 255, nullable = false)
  private String iconPath;

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "provider",
      orphanRemoval = true)
  private List<ExternalIdentityProviderPropertyTemplateEntity> properties = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getIdentityRequestBaseUrl() {
    return identityRequestBaseUrl;
  }

  public void setIdentityRequestBaseUrl(String baseUrl) {
    this.identityRequestBaseUrl = baseUrl;
  }

  public String getProfileRequestBaseUrl() {
    return profileRequestBaseUrl;
  }

  public void setProfileRequestBaseUrl(String profileRequestBaseUrl) {
    this.profileRequestBaseUrl = profileRequestBaseUrl;
  }

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(final String iconPath) {
    this.iconPath = iconPath;
  }
}
