/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.repository;

import software.iridium.api.entity.LoginDescriptorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginDescriptorEntityRepository extends JpaRepository<LoginDescriptorEntity, String> {

    Optional<LoginDescriptorEntity> findByTenantId(final String tenantId);

    Optional<LoginDescriptorEntity> findByTenant_Subdomain(final String subdomain);
}
