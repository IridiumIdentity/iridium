/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ClientSecretEntity;

@Component
public class ClientSecretEntityInstantiator {

  private static final Logger logger =
      LoggerFactory.getLogger(ClientSecretEntityInstantiator.class);

  @Resource private BCryptPasswordEncoder encoder;

  @Transactional(propagation = Propagation.REQUIRED)
  public ClientSecretEntity instantiate(final ApplicationEntity application, final String secret) {
    logger.info("generating secret for: " + application.getId());
    final var entity = new ClientSecretEntity();
    entity.setApplication(application);
    entity.setSecretKey(encoder.encode(secret));
    application.getClientSecrets().add(entity);
    return entity;
  }
}
