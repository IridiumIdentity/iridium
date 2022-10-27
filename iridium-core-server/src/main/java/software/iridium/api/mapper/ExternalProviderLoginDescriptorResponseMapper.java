/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.ExternalProviderLoginDescriptorResponse;
import software.iridium.api.entity.ExternalIdentityProviderEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExternalProviderLoginDescriptorResponseMapper {

    public List<ExternalProviderLoginDescriptorResponse> mapList(List<ExternalIdentityProviderEntity> providers) {
        final var responses = new ArrayList<ExternalProviderLoginDescriptorResponse>();
        for (ExternalIdentityProviderEntity provider: providers) {
            final var response = new ExternalProviderLoginDescriptorResponse();
            response.setScope(provider.getScope());
            response.setClientId(provider.getClientId());
            response.setIconPath(provider.getIconPath());
            response.setResponseType("code");
            response.setRedirectUri(provider.getRedirectUri());
            response.setScope(provider.getScope());
            response.setState("TheState"); // todo: fix me
            response.setName(provider.getName());
            responses.add(response);
        }
        return responses;
    }
}
