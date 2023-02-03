/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import software.iridium.api.entity.UuidIdentifiableAndAuditable;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "email_template_type_id"))
@Table(name = "email_template_types")
public class EmailTemplateTypeEntity extends UuidIdentifiableAndAuditable {

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = false)
  private String description;

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
}
