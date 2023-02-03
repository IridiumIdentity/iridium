/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.entity.LoginDescriptorEntity;
import software.iridium.api.entity.TenantEntity;

@Component
public class LoginDescriptorEntityInstantiator {

  @Transactional(propagation = Propagation.REQUIRED)
  public LoginDescriptorEntity instantiate(final TenantEntity tenant) {
    final var entity = new LoginDescriptorEntity();
    entity.setTenant(tenant);
    tenant.setLoginDescriptor(entity);
    return entity;
  }
}
