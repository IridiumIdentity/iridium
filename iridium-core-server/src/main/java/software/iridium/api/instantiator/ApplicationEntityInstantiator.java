/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import java.security.NoSuchAlgorithmException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ApplicationTypeEntity;
import software.iridium.api.util.EncoderUtils;

@Component
public class ApplicationEntityInstantiator {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationEntityInstantiator.class);

  public static final Integer CLIENT_ID_SEED_LENGTH = 16;

  @Resource private EncoderUtils encoderUtils;

  @Transactional(propagation = Propagation.REQUIRED)
  public ApplicationEntity instantiate(
      final ApplicationCreateRequest request,
      final ApplicationTypeEntity type,
      final String tenantId) {
    final var entity = new ApplicationEntity();
    entity.setApplicationType(type);
    entity.setTenantId(tenantId);
    entity.setName(request.getName());
    try {
      entity.setClientId(encoderUtils.cryptoSecureToHex(CLIENT_ID_SEED_LENGTH));
    } catch (NoSuchAlgorithmException e) {
      logger.error("error creating client id: ", e);
      throw new RuntimeException("error creating client id for application: " + type.getId());
    }
    return entity;
  }
}
