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
@AttributeOverride(name = "id", column = @Column(name = "application_type_id"))
@Table(name = "application_types")
public class ApplicationTypeEntity extends AbstractEntity {

  private static final long serialVersionUID = 1702011158548911399L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = false)
  private String description;

  @Column(name = "requires_secret", nullable = false)
  private Boolean requiresSecret;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private ApplicationType type;

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

  public Boolean requiresSecret() {
    return requiresSecret;
  }

  public Boolean doesNotRequireSecret() {
    return !requiresSecret;
  }

  public void setRequiresSecret(final Boolean requiresSecret) {
    this.requiresSecret = requiresSecret;
  }

  public ApplicationType getType() {
    return type;
  }

  public void setType(final ApplicationType type) {
    this.type = type;
  }
}
