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
import java.io.Serializable;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Auditable implements Serializable {

  private static final long serialVersionUID = -500382253621393740L;

  private static final String SERVER_ID = "SERVER_APP";

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", nullable = false)
  private Date created;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated", nullable = true)
  private Date updated;

  @Column(name = "create_id", length = 128, nullable = false)
  private String createId;

  @Column(name = "update_id", length = 128, nullable = true)
  private String updateId;

  @Column(name = "active", nullable = false)
  @Getter(AccessLevel.NONE)
  private Boolean active = true;

  /**
   * public Date getCreated() { return created; }
   *
   * <p>public void setCreated(Date created) { this.created = created; }
   *
   * <p>public Date getUpdated() { return updated; }
   *
   * <p>public void setUpdated(Date updated) { this.updated = updated; }
   *
   * <p>public String getCreateId() { return createId; }
   *
   * <p>public void setCreateId(String createId) { this.createId = createId; }
   *
   * <p>public String getUpdateId() { return updateId; }
   *
   * <p>public void setUpdateId(String updateId) { this.updateId = updateId; }
   *
   * <p>public Boolean getActive() { return active; }
   *
   * <p>public void setActive(Boolean active) { this.active = active; }
   */
  public boolean isActive() {
    return active != null && active;
  }

  @PrePersist
  public void prePersist() {
    if (created == null) {
      created = new Date();
    }
    createId = SERVER_ID;
  }

  @PreUpdate
  public void beforeUpdate() {
    updated = new Date();
    updateId = SERVER_ID;
  }
}
