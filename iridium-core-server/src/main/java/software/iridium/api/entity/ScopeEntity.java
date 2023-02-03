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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "scope_id"))
@Table(name = "scopes")
public class ScopeEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = -5985370597745504841L;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = false)
  private String description;

  @ManyToOne
  @JoinColumn(name = "application_id")
  private ApplicationEntity application;

  @ManyToOne
  @JoinColumn(name = "identity_id")
  private IdentityEntity identity;

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

  public ApplicationEntity getApplication() {
    return application;
  }

  public void setApplication(final ApplicationEntity application) {
    this.application = application;
  }

  public IdentityEntity getIdentity() {
    return identity;
  }

  public void setIdentity(final IdentityEntity identity) {
    this.identity = identity;
  }
}
