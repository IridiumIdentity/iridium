/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
