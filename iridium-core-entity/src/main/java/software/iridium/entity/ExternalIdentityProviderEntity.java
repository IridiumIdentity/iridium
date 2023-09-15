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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "external_identity_provider_id"))
@Table(name = "external_identity_providers")
public class ExternalIdentityProviderEntity extends AbstractEntity {
  private static final long serialVersionUID = 107595733607149226L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "client_id", length = 255, nullable = false)
  private String clientId;

  @Column(name = "client_secret", length = 255, nullable = true)
  private String clientSecret;

  @ManyToOne(optional = false)
  @JoinColumn(name = "external_identity_provider_template_id")
  private ExternalIdentityProviderTemplateEntity template;

  @ManyToOne(optional = false)
  @JoinColumn(name = "tenant_id")
  private TenantEntity tenant;

  @Column(name = "icon_path", length = 255, nullable = false)
  private String iconPath;

  @Column(name = "base_authorization_url", length = 2355, nullable = false)
  private String baseAuthorizationUrl;

  @Column(name = "scope", length = 255, nullable = true)
  private String scope;

  @Column(name = "access_token_request_base_url", length = 255, nullable = false)
  private String accessTokenRequestBaseUrl;

  @Column(name = "profile_request_base_url", length = 255, nullable = false)
  private String profileRequestBaseUrl;

  @OneToMany(
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      mappedBy = "provider",
      orphanRemoval = true)
  private List<ExternalIdentityProviderParameterEntity> properties = new ArrayList<>();

  @ElementCollection
  @CollectionTable(
      name = "authorization_parameters",
      joinColumns = {
        @JoinColumn(
            name = "external_identity_provider_id",
            referencedColumnName = "external_identity_provider_id")
      })
  @MapKeyColumn(name = "param_key")
  @Column(name = "param_value")
  private Map<String, String> authorizationParameters = new HashMap<>();

  @ElementCollection
  @CollectionTable(
      name = "access_token_parameters",
      joinColumns = {
        @JoinColumn(
            name = "external_identity_provider_id",
            referencedColumnName = "external_identity_provider_id")
      })
  @MapKeyColumn(name = "param_key")
  @Column(name = "param_value")
  private Map<String, String> accessTokenParameters = new HashMap<>();

  /**
   * public ExternalIdentityProviderTemplateEntity getTemplate() { return template; }
   *
   * <p>public void setTemplate(final ExternalIdentityProviderTemplateEntity template) {
   * this.template = template; }
   *
   * <p>public String getClientId() { return clientId; }
   *
   * <p>public void setClientId(final String clientId) { this.clientId = clientId; }
   *
   * <p>public String getClientSecret() { return clientSecret; }
   *
   * <p>public void setClientSecret(final String clientSecret) { this.clientSecret = clientSecret; }
   *
   * <p>public TenantEntity getTenant() { return tenant; }
   *
   * <p>public void setTenant(final TenantEntity tenant) { this.tenant = tenant; }
   *
   * <p>public List<ExternalIdentityProviderParameterEntity> getProperties() { return properties; }
   *
   * <p>public void setProperties(final List<ExternalIdentityProviderParameterEntity> properties) {
   * this.properties = properties; }
   *
   * <p>public String getAccessTokenRequestBaseUrl() { return accessTokenRequestBaseUrl; }
   *
   * <p>public void setAccessTokenRequestBaseUrl(final String identityRequestBaseUrl) {
   * this.accessTokenRequestBaseUrl = identityRequestBaseUrl; }
   *
   * <p>public String getProfileRequestBaseUrl() { return profileRequestBaseUrl; }
   *
   * <p>public void setProfileRequestBaseUrl(final String profileRequestBaseUrl) {
   * this.profileRequestBaseUrl = profileRequestBaseUrl; }
   *
   * <p>public String getBaseAuthorizationUrl() { return baseAuthorizationUrl; }
   *
   * <p>public void setBaseAuthorizationUrl(final String baseAuthorizationUrl) {
   * this.baseAuthorizationUrl = baseAuthorizationUrl; }
   *
   * <p>public String getScope() { return scope; }
   *
   * <p>public void setScope(final String scope) { this.scope = scope; }
   *
   * <p>public String getIconPath() { return iconPath; }
   *
   * <p>public void setIconPath(final String iconPath) { this.iconPath = iconPath; }
   *
   * <p>public String getName() { return name; }
   *
   * <p>public void setName(final String name) { this.name = name; }
   *
   * <p>public Map<String, String> getAuthorizationParameters() { return authorizationParameters; }
   *
   * <p>public void setAuthorizationParameters(final Map<String, String> authorizationParameters) {
   * this.authorizationParameters = authorizationParameters; }
   *
   * <p>public Map<String, String> getAccessTokenParameters() { return accessTokenParameters; }
   *
   * <p>public void setAccessTokenParameters(final Map<String, String> accessTokenParameters) {
   * this.accessTokenParameters = accessTokenParameters; }
   */
}
