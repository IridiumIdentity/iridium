/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.repository;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import software.iridium.api.entity.AuthenticationEntity;

public interface AuthenticationEntityRepository
    extends JpaRepository<AuthenticationEntity, String> {

  Optional<AuthenticationEntity> findFirstByIdentityIdOrderByCreatedDesc(final String userId);

  Optional<AuthenticationEntity> findFirstByAuthTokenAndExpirationAfter(
      final String token, final Date now);
}
