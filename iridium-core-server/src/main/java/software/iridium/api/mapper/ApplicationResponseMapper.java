/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.ApplicationCreateResponse;
import software.iridium.api.entity.ApplicationEntity;

@Component
public class ApplicationResponseMapper {

  public ApplicationCreateResponse map(final ApplicationEntity entity) {
    final var response = new ApplicationCreateResponse();
    response.setName(entity.getName());
    response.setApplicationTypeId(entity.getApplicationType().getId());
    response.setTenantId(entity.getTenantId());
    response.setId(entity.getId());
    return response;
  }
}
