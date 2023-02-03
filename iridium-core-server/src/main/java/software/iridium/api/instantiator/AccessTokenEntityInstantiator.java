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
import software.iridium.api.entity.AccessTokenEntity;
import software.iridium.api.util.DateUtils;
import software.iridium.api.util.TokenGenerator;

@Component
public class AccessTokenEntityInstantiator {

  private static final Integer HOURS_TO_EXPIRATION = 1;

  @Resource private TokenGenerator tokenGenerator;

  @Transactional(propagation = Propagation.REQUIRED)
  public AccessTokenEntity instantiate(final String identityId) {
    final var entity = new AccessTokenEntity();
    entity.setIdentityId(identityId);
    entity.setExpiration(DateUtils.addHoursToCurrentTime(1));
    entity.setTokenType("Bearer");
    final var accessToken =
        tokenGenerator.generateAccessToken(
            identityId, DateUtils.addHoursToCurrentTime(HOURS_TO_EXPIRATION));
    entity.setAccessToken(accessToken);
    // final var refreshToken = refreshTokenInstantiator.instantiate(entity);
    return entity;
  }
}
