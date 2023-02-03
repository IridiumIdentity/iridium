/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import javax.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "external_identity_provider_property_id"))
@Table(name = "external_identity_provider_properties")
public class ExternalIdentityProviderPropertyEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = 3475881474031747675L;

  @Column(name = "value")
  private String value;

  @Column(name = "name")
  private String name;

  @ManyToOne(optional = false)
  @JoinColumn(name = "external_identity_provider_id")
  private ExternalIdentityProviderEntity provider;

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

  public ExternalIdentityProviderEntity getProvider() {
    return provider;
  }

  public void setProvider(final ExternalIdentityProviderEntity provider) {
    this.provider = provider;
  }
}
