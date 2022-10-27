/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.repository;

import software.iridium.api.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentityEntityRepository extends JpaRepository<IdentityEntity, String> {

    Optional<IdentityEntity> findByProvider_IdAndExternalId(final String providerId, final String externalId);
}
