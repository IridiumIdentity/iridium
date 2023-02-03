/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.validator;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import software.iridium.api.base.error.BadRequestException;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@Component
public class ApplicationEntityAccessTokenRequestValidator {

  @Resource private AttributeValidator attributeValidator;

  public void validate(final ApplicationEntity application, final Map<String, String> params) {

    if (attributeValidator.isNotBlank(
        params.getOrDefault(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), ""))) {
      if (attributeValidator.doesNotEqual(
          application.getRedirectUri(),
          params.get(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue()))) {
        throw new BadRequestException("redirect_uri is not valid");
      }
    }
  }
}
