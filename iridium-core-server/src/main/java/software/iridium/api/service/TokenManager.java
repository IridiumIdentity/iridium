/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.service;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.repository.AuthenticationEntityRepository;

@Component
public class TokenManager {

  @Resource private AuthenticationEntityRepository authenticationEntityRepository;
  @Resource private AuthenticationGenerator authenticationGenerator;

  @Transactional(propagation = Propagation.REQUIRED)
  public ImmutablePair<String, String> getOrGenerateToken(IdentityEntity identityEntity) {
    final var authenticationOptional =
        authenticationEntityRepository.findFirstByIdentityIdOrderByCreatedDesc(
            identityEntity.getId());

    if (authenticationOptional.isEmpty()
        || new Date().after(authenticationOptional.get().getExpiration())) {
      final var generatedAuthentication =
          authenticationGenerator.generateAuthentication(identityEntity);
      authenticationEntityRepository.save(generatedAuthentication);
      return new ImmutablePair<>(
          generatedAuthentication.getAuthToken(), generatedAuthentication.getRefreshToken());
    }

    return new ImmutablePair<>(
        authenticationOptional.get().getAuthToken(),
        authenticationOptional.get().getRefreshToken());
  }
}
