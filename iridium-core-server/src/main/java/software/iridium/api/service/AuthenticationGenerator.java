/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.service;

import software.iridium.api.util.DateUtils;
import software.iridium.api.entity.AuthenticationEntity;
import software.iridium.api.entity.IdentityEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class AuthenticationGenerator {

    @Resource
    private BCryptPasswordEncoder encoder;
    @Value("${software.iridium.api.ttl.minutes}")
    private Integer tokenTimeToLiveInMinutes;



    public AuthenticationEntity generateAuthentication(final IdentityEntity identityEntity) {
        final var authEntity = new AuthenticationEntity();
        authEntity.setIdentity(identityEntity);
        final var expiration = DateUtils.addHoursToCurrentTime(tokenTimeToLiveInMinutes);
        authEntity.setExpiration(expiration);
        authEntity.setAuthToken(generateAuthToken(identityEntity, expiration));
        authEntity.setRefreshToken(generateRefreshToken(authEntity.getAuthToken()));
        return authEntity;
    }

    public String generateAuthToken(final IdentityEntity identityEntity, final Date expiration) {
        var formattedExpiration = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S").format(expiration);
        var tokenSource = new StringBuilder().append(identityEntity.getPrimaryEmail().getEmailAddress())
                .append("||").append(formattedExpiration)
                .append("||").append(UUID.randomUUID())
                .toString();
        return encoder.encode(tokenSource);
    }

    public String generateRefreshToken(final String authToken) {
        return encoder.encode(authToken);
    }
}
