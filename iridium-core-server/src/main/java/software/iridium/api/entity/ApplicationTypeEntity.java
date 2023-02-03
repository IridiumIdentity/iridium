/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "application_type_id"))
@Table(name = "application_types")
public class ApplicationTypeEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = 1702011158548911399L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = false)
  private String description;

  @Column(name = "requires_secret", nullable = false)
  private Boolean requiresSecret;

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
}
