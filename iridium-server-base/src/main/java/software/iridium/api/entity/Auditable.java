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
import java.io.Serializable;
import java.util.Date;

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
  private Boolean active = true;

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public String getCreateId() {
    return createId;
  }

  public void setCreateId(String createId) {
    this.createId = createId;
  }

  public String getUpdateId() {
    return updateId;
  }

  public void setUpdateId(String updateId) {
    this.updateId = updateId;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

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
