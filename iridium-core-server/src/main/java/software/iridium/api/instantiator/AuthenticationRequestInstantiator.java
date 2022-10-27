/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationRequestInstantiator {

    public AuthenticationRequest instantiate(final CreateIdentityRequest request) {
        final var authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(request.getUsername());
        authenticationRequest.setPassword(request.getPassword());
        authenticationRequest.setClientId(request.getClientId());
        return authenticationRequest;
    }
}
