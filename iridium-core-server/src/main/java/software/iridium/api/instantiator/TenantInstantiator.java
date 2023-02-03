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
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.entity.TenantEntity;

@Component
public class TenantInstantiator {

  @Transactional(propagation = Propagation.REQUIRED)
  public TenantEntity instantiate(final CreateTenantRequest request) {
    final var entity = new TenantEntity();
    entity.setSubdomain(request.getSubdomain());
    entity.setEnvironment(request.getEnvironment());
    return entity;
  }
}
