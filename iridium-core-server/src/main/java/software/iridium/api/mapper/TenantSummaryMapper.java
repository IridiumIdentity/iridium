/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.entity.TenantEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TenantSummaryMapper {

    public List<TenantSummary> mapToList(final List<TenantEntity> entities) {
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::map).collect(Collectors.toList());
    }

    public TenantSummary map(final TenantEntity entity) {
        return TenantSummary.of(entity.getId(), entity.getSubdomain());
    }
}
