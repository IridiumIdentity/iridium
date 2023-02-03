/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.entity.AuthorizationCodeEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.DateUtils;
import software.iridium.api.util.EncoderUtils;

@Component
public class AuthorizationCodeEntityInstantiator {

  private static final Logger logger =
      LoggerFactory.getLogger(AuthorizationCodeEntityInstantiator.class);
  public static final Integer AUTHORIZATION_CODE_MAX_LENGTH = 24;
  public static final Integer AUTHORIZATION_CODE_EXPIRATION_IN_MINUTES = 1;

  @Resource private EncoderUtils encoderUtils;

  @Transactional(propagation = Propagation.REQUIRED)
  public AuthorizationCodeEntity instantiate(
      final IdentityEntity identity, final Map<String, String> params) {
    logger.info("instantiating auth code for: {}", identity.getId());
    final var entity = new AuthorizationCodeEntity();
    entity.setClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()));
    entity.setIdentityId(identity.getId());
    final var codeChallengeMethodStr =
        params.get(AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue());
    entity.setCodeChallengeMethod(CodeChallengeMethod.valueOf(codeChallengeMethodStr));
    final var codeChallenge = params.get(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue());
    entity.setCodeChallenge(codeChallenge);
    entity.setRedirectUri(params.get(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue()));
    entity.setAuthorizationCode(
        encoderUtils.generateCryptoSecureString(AUTHORIZATION_CODE_MAX_LENGTH));
    // todo: make this a component for easier testing
    entity.setExpiration(
        DateUtils.addMinutesToCurrentTime(AUTHORIZATION_CODE_EXPIRATION_IN_MINUTES));
    return entity;
  }
}
