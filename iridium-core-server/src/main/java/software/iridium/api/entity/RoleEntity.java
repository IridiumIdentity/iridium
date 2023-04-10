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
import java.util.HashSet;
import java.util.Set;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "group_id"))
@Table(name = "roles")
public class RoleEntity extends AbstractEntity {

  private static final long serialVersionUID = 3782334153369269308L;

  @Column(name = "name", length = 128, nullable = false, unique = true)
  private String name;

  @Column(name = "description", length = 255, nullable = true)
  private String description;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private Set<IdentityEntity> identities = new HashSet<>();

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

  public Set<IdentityEntity> getIdentities() {
    return identities;
  }

  public void setIdentities(final Set<IdentityEntity> identities) {
    this.identities = identities;
  }
}
