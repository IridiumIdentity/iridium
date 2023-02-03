/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.validator;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@Component
public class AuthenticationRequestParamValidator {

  @Resource private AttributeValidator attributeValidator;

  public void validate(final Map<String, String> params) {
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), "")),
        "clientId must not be blank");
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(
                AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), "")),
        "code_challenge_method must not be blank");
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue(), "")),
        "code_challenge must not be blank");
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), "")),
        "redirect_uri must not be blank");
  }
}
