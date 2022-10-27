/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.repository;

import software.iridium.api.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TenantEntityRepository extends JpaRepository<TenantEntity, String> {

    Optional<List<TenantEntity>> findByIdIn(final List<String> ids);

    Optional<TenantEntity> findBySubdomain(final String subdomain);
}
