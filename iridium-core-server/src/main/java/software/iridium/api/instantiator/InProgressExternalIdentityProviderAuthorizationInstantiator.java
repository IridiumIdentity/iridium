/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.entity.ExternalIdentityProviderEntity;
import software.iridium.api.entity.InProgressExternalIdentityProviderAuthorizationEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Component
public class InProgressExternalIdentityProviderAuthorizationInstantiator {


    @Transactional(propagation = Propagation.REQUIRED)
    public InProgressExternalIdentityProviderAuthorizationEntity instantiate(
            final ExternalIdentityProviderEntity provider,
            final String state,
            final String redirectUri,
            final String clientId
            ) {
        final var entity = new InProgressExternalIdentityProviderAuthorizationEntity();
        entity.setProvider(provider);
        entity.setClientId(clientId);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MINUTE, 10);
        entity.setExpiration(c.getTime());
        entity.setState(state);
        entity.setRedirectUri(redirectUri);
        return entity;
    }
}
