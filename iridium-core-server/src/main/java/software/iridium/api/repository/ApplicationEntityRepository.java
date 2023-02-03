/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import software.iridium.api.entity.ApplicationEntity;

public interface ApplicationEntityRepository
    extends PagingAndSortingRepository<ApplicationEntity, String> {

  Optional<ApplicationEntity> findByNameAndTenantId(final String id, final String tenantId);

  Optional<ApplicationEntity> findByClientId(final String clientId);

  Page<ApplicationEntity> findAllByTenantIdAndApplicationTypeIdAndActive(
      final String tenantId,
      final String applicationTypeId,
      final Boolean active,
      final Pageable of);
}
