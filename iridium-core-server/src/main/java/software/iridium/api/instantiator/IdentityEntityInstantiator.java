/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.instantiator;

import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.entity.ExternalIdentityProviderEntity;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
public class IdentityEntityInstantiator {

    @Resource
    private EmailEntityInstantiator emailInstantiator;
    @Resource
    private IdentityPropertyEntityInstantiator propertyInstantiator;

    @Transactional(propagation = Propagation.REQUIRED)
    public IdentityEntity instantiate(final CreateIdentityRequest request, final String encodedTempPassword, final String tenantId) {
        final IdentityEntity entity = instantiateIdentityAndAssociate(request.getUsername());
        entity.setEncodedPassword(encodedTempPassword);
        entity.setParentTenantId(tenantId);
        return entity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public IdentityEntity instantiateFromGithub(final GithubProfileResponse response, final ExternalIdentityProviderEntity provider) {
        final IdentityEntity identity = instantiateIdentityAndAssociate(response.getEmail());
        identity.setProvider(provider);
        identity.setExternalId(response.getId());
        propertyInstantiator.instantiateGithubProperties(response, identity);
        return identity;
    }

    private IdentityEntity instantiateIdentityAndAssociate(final String response) {
        final var identity = new IdentityEntity();
        final var email = emailInstantiator.instantiatePrimaryEmail(response);
        associate(identity, email);
        return identity;
    }

    private void associate(final IdentityEntity entity, final IdentityEmailEntity email) {
        entity.getEmails().add(email);
        email.setIdentity(entity);
    }
}
