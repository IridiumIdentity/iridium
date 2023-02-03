/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Auditable implements Serializable {

  private static final long serialVersionUID = -500382253621393740L;

  private static final String WEB_ID = "SERVER_APP";

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
    createId = WEB_ID;
  }

  @PreUpdate
  public void beforeUpdate() {
    updated = new Date();
    updateId = WEB_ID;
  }
}
