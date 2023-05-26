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
import software.iridium.api.authentication.domain.Environment;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "tenant_id"))
@Table(name = "tenants")
public class TenantEntity extends AbstractEntity {

  private static final long serialVersionUID = 689089741127267195L;

  @Column(name = "subdomain", length = 100, nullable = false, unique = true, updatable = false)
  private String subdomain;

  @ManyToMany(mappedBy = "managedTenants", fetch = FetchType.LAZY)
  private List<IdentityEntity> managingIdentities = new ArrayList<>();

  @Column(name = "website_url", length = 255, nullable = true, unique = true, updatable = true)
  private String websiteUrl;

  @Column(name = "environment")
  @Enumerated(EnumType.STRING)
  private Environment environment;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "login_descriptor_id")
  private LoginDescriptorEntity loginDescriptor;

  @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY)
  private List<ExternalIdentityProviderEntity> externalIdentityProviders = new ArrayList<>();

  public List<IdentityEntity> getManagingIdentities() {
    return managingIdentities;
  }

  public void setManagingIdentities(final List<IdentityEntity> identities) {
    this.managingIdentities = identities;
  }

  public String getSubdomain() {
    return subdomain;
  }

  public void setSubdomain(final String subdomain) {
    this.subdomain = subdomain;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(final Environment environment) {
    this.environment = environment;
  }

  public LoginDescriptorEntity getLoginDescriptor() {
    return loginDescriptor;
  }

  public void setLoginDescriptor(final LoginDescriptorEntity loginDescriptor) {
    this.loginDescriptor = loginDescriptor;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(final String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public List<ExternalIdentityProviderEntity> getExternalIdentityProviders() {
    return externalIdentityProviders;
  }

  public void setExternalIdentityProviders(
      final List<ExternalIdentityProviderEntity> externalIdentityProviders) {
    this.externalIdentityProviders = externalIdentityProviders;
  }
}
