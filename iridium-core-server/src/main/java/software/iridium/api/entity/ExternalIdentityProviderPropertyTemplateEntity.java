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

import jakarta.persistence.*;

@Entity
@AttributeOverride(
    name = "id",
    column = @Column(name = "external_identity_provider_property_template_id"))
@Table(name = "external_identity_provider_property_templates")
public class ExternalIdentityProviderPropertyTemplateEntity extends AbstractEntity {

  @Column(name = "value")
  private String value;

  @Column(name = "name")
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(name = "external_identity_provider_template_id")
  private ExternalIdentityProviderTemplateEntity provider;

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public ExternalIdentityProviderTemplateEntity getProvider() {
    return provider;
  }

  public void setProvider(final ExternalIdentityProviderTemplateEntity provider) {
    this.provider = provider;
  }
}
