/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.repository;

import software.iridium.api.entity.ClientSecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientSecretEntityRepository extends JpaRepository<ClientSecretEntity, String> {
    // intentionally left blank
}
