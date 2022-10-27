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
package software.iridium.api.entity;

import java.util.*;
import javax.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "application_id"))
@Table(name = "applications")
public class ApplicationEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = -1225093340932226182L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = true)
  private String description;

  @Column(name = "home_page_url", length = 255, nullable = true)
  private String homePageUrl;

  @Column(name = "privacy_policy_url", length = 255, nullable = true)
  private String privacyPolicyUrl;

  @Column(name = "redirect_uri", length = 255, nullable = true)
  private String redirectUri;

  @Column(name = "icon_url", length = 255, nullable = true)
  private String iconUrl;

  @Column(name = "tenant_id", length = 36, nullable = false)
  private String tenantId;

  @Column(name = "client_id", length = 32, nullable = false)
  private String clientId;

  @ManyToMany(mappedBy = "authorizedApplications")
  private Set<IdentityEntity> identities = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "application_type_id")
  private ApplicationTypeEntity applicationType;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ClientSecretEntity> clientSecrets = new ArrayList<>();

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ScopeEntity> scopes = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getHomePageUrl() {
    return homePageUrl;
  }

  public void setHomePageUrl(final String homePageUrl) {
    this.homePageUrl = homePageUrl;
  }

  public String getPrivacyPolicyUrl() {
    return privacyPolicyUrl;
  }

  public void setPrivacyPolicyUrl(final String privacyPolicyUrl) {
    this.privacyPolicyUrl = privacyPolicyUrl;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(final String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }

  public ApplicationTypeEntity getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(final ApplicationTypeEntity applicationType) {
    this.applicationType = applicationType;
  }

  public List<ClientSecretEntity> getClientSecrets() {
    return clientSecrets;
  }

  public void setClientSecrets(final List<ClientSecretEntity> clientSecrets) {
    this.clientSecrets = clientSecrets;
  }

  public List<ScopeEntity> getScopes() {
    return scopes;
  }

  public void setScopes(final List<ScopeEntity> scopes) {
    this.scopes = scopes;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(final String authorizationCallbackUrl) {
    this.redirectUri = authorizationCallbackUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public Set<IdentityEntity> getIdentities() {
    return identities;
  }

  public void setIdentities(final Set<IdentityEntity> identities) {
    this.identities = identities;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (this == o) {
      return true;
    }
    if (o instanceof ApplicationEntity) {
      ApplicationEntity that = (ApplicationEntity) o;
      return Objects.equals(this.getId(), that.getId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
