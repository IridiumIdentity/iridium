/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import software.iridium.api.entity.TenantEntity;

public interface TenantEntityRepository extends JpaRepository<TenantEntity, String> {

  Optional<List<TenantEntity>> findByIdIn(final List<String> ids);

  Optional<TenantEntity> findBySubdomain(final String subdomain);
}
