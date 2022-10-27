/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.ProviderSummaryResponse;
import software.iridium.api.entity.ExternalIdentityProviderTemplateEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProviderSummaryResponseMapper {

    public List<ProviderSummaryResponse> mapList(final List<ExternalIdentityProviderTemplateEntity> entities) {
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream().map(this::map).collect(Collectors.toList());
    }

    public ProviderSummaryResponse map(final ExternalIdentityProviderTemplateEntity entity) {
        final var summary = new ProviderSummaryResponse();
        summary.setId(entity.getId());
        summary.setName(entity.getName());
        summary.setIconPath(entity.getIconPath());
        return summary;
    }
}
