/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.AuthenticationResponse;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.entity.IdentityEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class IdentityResponseMapper {

    @Resource
    private ProfileResponseMapper profileMapper;

    public IdentityResponse map(final IdentityEntity entity, final AuthenticationResponse authenticationResponse) {
        final var response = new IdentityResponse();
        response.setId(entity.getId());
        response.setUsername(entity.getPrimaryEmail().getEmailAddress());
        response.setProfile(profileMapper.map(entity.getProfile()));
        response.setAppBaseUrl(authenticationResponse.getAppBaseurl());
        response.setTenantWebsite(authenticationResponse.getTenantWebsite());
        response.setApplicationName(authenticationResponse.getApplicationName());
        response.setUserToken(authenticationResponse.getUserToken());
        return response;
    }

    public IdentityResponse map(final IdentityEntity entity) {
        final var response = new IdentityResponse();
        response.setId(entity.getId());
        response.setUsername(entity.getPrimaryEmail().getEmailAddress());
        response.setProfile(profileMapper.map(entity.getProfile()));
        return response;
    }
}
