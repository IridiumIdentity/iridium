/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.entity.IdentityCreateSessionDetails;
import software.iridium.api.entity.IdentityEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class IdentityCreateRequestDetailsInstantiator {

    @Transactional(propagation = Propagation.REQUIRED)
    public IdentityCreateSessionDetails instantiate(final Map<String, String> requestParamMap, final IdentityEntity identity) {
        final var entity = new IdentityCreateSessionDetails();
        entity.setCodeChallenge(requestParamMap.get("code_challenge"));
        entity.setCodeChallengeMethod(CodeChallengeMethod.valueOf(requestParamMap.get("code_challenge_method")));
        entity.setClientId(requestParamMap.get("client_id"));
        entity.setState(requestParamMap.get("state"));
        entity.setResponseType(requestParamMap.get("response_type"));
        entity.setRedirectUri(requestParamMap.get("redirect_uri"));
        entity.setIdentity(identity);
        return entity;
    }
}
