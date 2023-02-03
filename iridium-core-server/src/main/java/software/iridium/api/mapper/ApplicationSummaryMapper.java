/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import software.iridium.api.authentication.domain.ApplicationSummary;
import software.iridium.api.entity.ApplicationEntity;

@Component
public class ApplicationSummaryMapper {

  public List<ApplicationSummary> mapToSummaries(final Collection<ApplicationEntity> entities) {
    if (CollectionUtils.isEmpty(entities)) {
      return Collections.emptyList();
    }
    return entities.stream().map(this::mapToSummary).collect(Collectors.toList());
  }

  public ApplicationSummary mapToSummary(final ApplicationEntity entity) {
    final var response = new ApplicationSummary();
    response.setId(entity.getId());
    response.setName(entity.getName());
    response.setIconUrl(entity.getIconUrl());
    return response;
  }
}
