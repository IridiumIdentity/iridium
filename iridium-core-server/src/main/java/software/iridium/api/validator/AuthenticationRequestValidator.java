/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.validator;

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.util.AttributeValidator;

@Component
public class AuthenticationRequestValidator {

  @Resource private AttributeValidator attributeValidator;

  public void validate(final AuthenticationRequest request) {
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getUsername(), 100),
        "username must not blank and no longer than 100 characters: " + request.getUsername());
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getPassword(), 100),
        "password must not blank and no longer than 100 characters");
    checkArgument(
        attributeValidator.isNotBlankAndNoLongerThan(request.getClientId(), 32),
        "clientId must not be blank and no longer than 32 characters");
  }
}
