/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.repository;

import software.iridium.api.entity.AuthorizationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationCodeEntityRepository extends JpaRepository<AuthorizationCodeEntity, String> {

    Optional<AuthorizationCodeEntity> findByAuthorizationCodeAndActiveTrue(final String authorizationCode);
}
