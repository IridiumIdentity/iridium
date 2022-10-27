/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.service;


import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.AuthorizationCodeEntityInstantiator;
import software.iridium.api.repository.*;
import software.iridium.api.validator.AuthenticationRequestParamValidator;
import software.iridium.api.validator.AuthenticationRequestValidator;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.AuthenticationResponse;
import software.iridium.api.entity.ApplicationEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class AuthenticationService {

    @Resource
    private IdentityEntityRepository identityRepository;
    @Resource
    private BCryptPasswordEncoder encoder;
    @Resource
    private TokenManager tokenManager;
    @Resource
    private IdentityEmailEntityRepository emailRepository;
    @Resource
    private AuthenticationRequestValidator authenticationRequestValidator;
    @Resource
    private AuthenticationRequestParamValidator authRequestParamValidator;
    @Resource
    private ApplicationEntityRepository applicationRepository;
    @Resource
    private TenantEntityRepository tenantRepository;
    @Resource
    private AuthorizationCodeEntityInstantiator authCodeInstantiator;
    @Resource
    private AuthorizationCodeEntityRepository authCodeEntityRepository;

    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NotAuthorizedException.class)
    public AuthenticationResponse authenticate(final AuthenticationRequest request, final Map<String, String> params) {
        authenticationRequestValidator.validate(request);
        authRequestParamValidator.validate(params);

        var application = applicationRepository.findByClientId(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("application not found for client id: " + request.getClientId()));
        var emailOptional = emailRepository.findByEmailAddressAndIdentity_ParentTenantId(request.getUsername(), application.getTenantId());
        if (emailOptional.isPresent()) {
            var identity = emailOptional.get().getIdentity();
            if (identity.isNotLocked() &&
                            encoder.matches(request.getPassword(), identity.getEncodedPassword())) {

                var tokens = tokenManager.getOrGenerateToken(identity);
                boolean isAuthorized = false;

                for (ApplicationEntity authorizedApplication: identity.getAuthorizedApplications()) {
                    if (application.equals(authorizedApplication)) {
                        isAuthorized = true;
                        break;
                    }
                }

                if (identity.doesNotRequirePasswordChange()) {
                    final var tenant = tenantRepository.findById(application.getTenantId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Tenant not found for id: " + application.getTenantId()));
                    if (isAuthorized) {
                        final var authCode = authCodeInstantiator.instantiate(identity, params);
                        authCodeEntityRepository.save(authCode);
                        identity.setLastSuccessfulLogin(new Date());
                        identity.setFailedLoginAttempts(0);
                        identityRepository.save(identity);
                        return AuthenticationResponse.of(tokens.getLeft(), tokens.getRight(), isAuthorized, application.getName(), tenant.getWebsiteUrl(), application.getHomePageUrl(), application.getRedirectUri(), authCode.getAuthorizationCode());

                    }
                    identity.setLastSuccessfulLogin(new Date());
                    identity.setFailedLoginAttempts(0);
                    identityRepository.save(identity);
                    return AuthenticationResponse.of(tokens.getLeft(), tokens.getRight(), isAuthorized, application.getName(), tenant.getWebsiteUrl(), application.getHomePageUrl(), application.getRedirectUri(), null);
                }
            }
            identity.setFailedLoginAttempts(identity.getFailedLoginAttempts() + 1);
            identityRepository.save(identity);
        }
        throw new NotAuthorizedException();
    }
}
