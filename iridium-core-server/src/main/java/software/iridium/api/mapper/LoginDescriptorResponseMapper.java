/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.entity.LoginDescriptorEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginDescriptorResponseMapper {

    @Resource
    ExternalProviderLoginDescriptorResponseMapper externalProviderDescriptorMapper;

    public LoginDescriptorResponse map(final LoginDescriptorEntity entity) {

        final var response = new LoginDescriptorResponse();
        response.setExternalProviderDescriptors(externalProviderDescriptorMapper.mapList(entity.getTenant().getExternalIdentityProviders()));
        response.setIconPath(entity.getIconPath());
        response.setPageTitle(entity.getPageTitle());
        response.setDisplayName(entity.getDisplayName());
        response.setUsernameLabel(entity.getUsernameLabel());
        response.setUsernameErrorHint(entity.getUsernameErrorHint());
        response.setUsernameType(entity.getUsernameType());
        response.setUsernamePlaceholder(entity.getUsernamePlaceholder());
        return response;
    }
}
