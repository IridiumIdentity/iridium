/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.mapper;

import software.iridium.api.authentication.domain.ApplicationTypeSummary;
import software.iridium.api.entity.ApplicationTypeEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class  ApplicationTypeSummaryMapper {

    public List<ApplicationTypeSummary> mapToList(final List<ApplicationTypeEntity> entities) {
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::map).collect(Collectors.toList());
    }

    public ApplicationTypeSummary map(final ApplicationTypeEntity entity) {
        return ApplicationTypeSummary.of(entity.getId(), entity.getName(), entity.getDescription());
    }
}
