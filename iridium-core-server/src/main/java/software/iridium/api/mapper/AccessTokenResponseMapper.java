/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.AccessTokenResponse;
import software.iridium.api.entity.AccessTokenEntity;

@Component
public class AccessTokenResponseMapper {

  public AccessTokenResponse map(final AccessTokenEntity entity) {
    final var response = new AccessTokenResponse();
    response.setAccessToken(entity.getAccessToken());
    // response.setRefreshToken(entity.getRefreshToken().getRefreshToken());
    response.setTokenType(entity.getTokenType());
    response.setExpiresIn(3600L);
    return response;
  }
}
