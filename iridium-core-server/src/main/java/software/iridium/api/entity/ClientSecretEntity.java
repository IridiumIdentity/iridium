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
@AttributeOverride(name = "id", column = @Column(name = "client_secret_id"))
@Table(name = "client_secrets")
public class ClientSecretEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = -2768574776374607276L;

  @Column(name = "secret_key", updatable = false, nullable = false)
  private String secretKey;

  @ManyToOne
  @JoinColumn(name = "application_id")
  private ApplicationEntity application;

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(final String secretKey) {
    this.secretKey = secretKey;
  }

  public ApplicationEntity getApplication() {
    return application;
  }

  public void setApplication(final ApplicationEntity application) {
    this.application = application;
  }
}
