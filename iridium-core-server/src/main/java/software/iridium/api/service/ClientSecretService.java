/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.service;

import static com.google.common.base.Preconditions.checkArgument;

import java.security.NoSuchAlgorithmException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ClientSecretCreateResponse;
import software.iridium.api.base.error.ClientCallException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.ClientSecretEntityInstantiator;
import software.iridium.api.mapper.ClientSecretCreateResponseMapper;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.ClientSecretEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.EncoderUtils;

@Service
public class ClientSecretService {

  private static final Logger logger = LoggerFactory.getLogger(ClientSecretService.class);
  public static final Integer SEED_LENGTH = 32;

  @Resource private AttributeValidator attributeValidator;
  @Resource private ApplicationEntityRepository applicationRepository;
  @Resource private ClientSecretEntityInstantiator clientSecretInstantiator;
  @Resource private ClientSecretCreateResponseMapper responseMapper;
  @Resource private ClientSecretEntityRepository clientSecretRepository;
  @Resource private EncoderUtils encoderUtils;

  @Transactional(propagation = Propagation.REQUIRED)
  public ClientSecretCreateResponse create(final String applicationId) {
    checkArgument(attributeValidator.isUuid(applicationId), "applicationId must be a valid uuid");

    final var application =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "application not found for id: " + applicationId));

    if (application.getApplicationType().doesNotRequireSecret()) {
      logger.error(
          "incorrect attempt to create secret for non secret application type {}",
          application.getId());
      throw new ClientCallException(
          "incorrect attempt to create secret for non secret application: " + application.getId());
    }

    try {
      final var clearSecret = encoderUtils.cryptoSecureToHex(SEED_LENGTH);
      final var clientSecret = clientSecretInstantiator.instantiate(application, clearSecret);
      return responseMapper.map(clientSecret, clearSecret);
    } catch (NoSuchAlgorithmException e) {
      logger.error("error creating secret for application", e);
      throw new RuntimeException("error creating secret for application");
    }
  }

  public void delete(final String applicationId, final String clientSecretId) {
    checkArgument(attributeValidator.isUuid(applicationId), "applicationId must be a valid uuid");
    checkArgument(attributeValidator.isUuid(clientSecretId), "clientSecretId must be a valid uuid");

    final var clientSecret =
        clientSecretRepository
            .findById(clientSecretId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "client secret not found for id: " + clientSecretId));

    if (clientSecret.getApplication().getId().equals(applicationId)) {
      clientSecretRepository.delete(clientSecret);
      return;
    }
    throw new IllegalArgumentException(
        String.format(
            "client secret %s not associated with application %s", clientSecretId, applicationId));
  }
}
