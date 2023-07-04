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
import java.util.ArrayList;
import java.util.List;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "external_identity_provider_template_id"))
@Table(name = "external_identity_provider_templates")
public class ExternalIdentityProviderTemplateEntity extends AbstractEntity {

  private static final long serialVersionUID = -812177502861094777L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "access_token_request_base_url", length = 255, nullable = false)
  private String accessTokenRequestBaseUrl;

  @Column(name = "profile_request_base_url", length = 255, nullable = false)
  private String profileRequestBaseUrl;

  @Column(name = "icon_path", length = 255, nullable = false)
  private String iconPath;

  @Column(name = "base_authorization_url", length = 2355, nullable = false)
  private String baseAuthorizationUrl;

  @Column(name = "default_scope", length = 2048, nullable = false)
  private String defaultScope;

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "provider",
      orphanRemoval = true)
  private List<ExternalIdentityProviderParameterTemplateEntity> accessTokenParameters =
      new ArrayList<>();

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "provider",
      orphanRemoval = true)
  private List<ExternalIdentityProviderParameterTemplateEntity> authorizationParameters =
      new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getAccessTokenRequestBaseUrl() {
    return accessTokenRequestBaseUrl;
  }

  public void setAccessTokenRequestBaseUrl(String baseUrl) {
    this.accessTokenRequestBaseUrl = baseUrl;
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

  public String getBaseAuthorizationUrl() {
    return baseAuthorizationUrl;
  }

  public void setBaseAuthorizationUrl(final String baseAuthorizationUrl) {
    this.baseAuthorizationUrl = baseAuthorizationUrl;
  }

  public List<ExternalIdentityProviderParameterTemplateEntity> getAuthorizationParameters() {
    return authorizationParameters;
  }

  public void setAuthorizationParameters(
      final List<ExternalIdentityProviderParameterTemplateEntity> properties) {
    this.authorizationParameters = properties;
  }

  public List<ExternalIdentityProviderParameterTemplateEntity> getAccessTokenParameters() {
    return accessTokenParameters;
  }

  public void setAccessTokenParameters(
      final List<ExternalIdentityProviderParameterTemplateEntity> accessTokenParameters) {
    this.accessTokenParameters = accessTokenParameters;
  }

  public String getDefaultScope() {
    return defaultScope;
  }

  public void setDefaultScope(final String defaultScope) {
    this.defaultScope = defaultScope;
  }
}
