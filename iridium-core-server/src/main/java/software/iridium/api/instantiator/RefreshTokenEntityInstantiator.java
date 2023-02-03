/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.entity.RefreshTokenEntity;
import software.iridium.api.util.TokenGenerator;

@Component
public class RefreshTokenEntityInstantiator {

  @Resource private TokenGenerator tokenGenerator;

  @Transactional(propagation = Propagation.REQUIRED)
  public RefreshTokenEntity instantiate(final String accessToken) {
    final var entity = new RefreshTokenEntity();
    entity.setRefreshToken(tokenGenerator.generateRefreshToken(accessToken));
    return entity;
  }
}
