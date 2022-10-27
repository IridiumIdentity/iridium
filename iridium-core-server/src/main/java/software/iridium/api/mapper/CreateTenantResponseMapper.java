/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.CreateTenantResponse;
import software.iridium.api.entity.TenantEntity;
import org.springframework.stereotype.Component;

@Component
public class CreateTenantResponseMapper {

    public CreateTenantResponse map(final TenantEntity entity) {
        final var response = new CreateTenantResponse();
        response.setId(entity.getId());
        response.setSubdomain(entity.getSubdomain());
        response.setEnvironment(entity.getEnvironment());
        return response;
    }
}
