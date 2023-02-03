/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.validator;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@Component
public class AuthorizationGrantTypeParamValidator {

  @Resource private AttributeValidator attributeValidator;

  public void validate(final Map<String, String> params) {
    checkArgument(
        attributeValidator.isNotBlank(
            params.getOrDefault(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(), "")),
        "response_type must not be blank");
    if (Objects.equals(
        params.get(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue()),
        AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue())) {
      checkArgument(
          attributeValidator.isNotBlank(
              params.getOrDefault(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), "")),
          "client_id must not be blank");
    }
  }
}
