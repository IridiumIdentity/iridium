/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.ClientSecretCreateResponse;
import software.iridium.api.entity.ClientSecretEntity;

@Component
public class ClientSecretCreateResponseMapper {
  public ClientSecretCreateResponse map(
      final ClientSecretEntity entity, final String clearTextSecret) {
    final var response = new ClientSecretCreateResponse();
    response.setId(entity.getId());
    response.setSecretKey(clearTextSecret);
    response.setCreated(entity.getCreated());
    return response;
  }
}
