/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.entity.RefreshTokenEntity;
import software.iridium.api.util.TokenGenerator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class RefreshTokenEntityInstantiator {

    @Resource
    private TokenGenerator tokenGenerator;

    @Transactional(propagation = Propagation.REQUIRED)
    public RefreshTokenEntity instantiate(final String accessToken) {
        final var entity = new RefreshTokenEntity();
        entity.setRefreshToken(tokenGenerator.generateRefreshToken(accessToken));
        return entity;
    }
}
