/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.entity.IdentityEmailEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmailEntityInstantiator {

    @Transactional(propagation = Propagation.REQUIRED)
    public IdentityEmailEntity instantiatePrimaryEmail(final String emailAddress) {
        IdentityEmailEntity email = new IdentityEmailEntity();
        email.setEmailAddress(emailAddress);
        email.setPrimary(true);
        return email;
    }
}
