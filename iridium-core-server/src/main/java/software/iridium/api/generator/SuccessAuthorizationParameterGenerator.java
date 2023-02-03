/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.generator;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

@Component
public class SuccessAuthorizationParameterGenerator {

  public LinkedMultiValueMap<String, String> generate(
      final Map<String, String> params, final String authCode) {
    final var responseParamMap = new LinkedMultiValueMap<String, String>();
    responseParamMap.put("code", List.of(authCode));
    responseParamMap.put(
        "state", List.of(params.get(AuthorizationCodeFlowConstants.STATE.getValue())));
    return responseParamMap;
  }
}
