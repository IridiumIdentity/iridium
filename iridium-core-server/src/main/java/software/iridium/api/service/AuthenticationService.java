/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api.service;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.AuthenticationResponse;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.instantiator.AuthorizationCodeEntityInstantiator;
import software.iridium.api.repository.*;
import software.iridium.api.validator.AuthenticationRequestParamValidator;
import software.iridium.api.validator.AuthenticationRequestValidator;

@Service
public class AuthenticationService {

  @Autowired private IdentityEntityRepository identityRepository;
  @Autowired private BCryptPasswordEncoder encoder;
  @Autowired private TokenManager tokenManager;
  @Autowired private IdentityEmailEntityRepository emailRepository;
  @Autowired private AuthenticationRequestValidator authenticationRequestValidator;
  @Autowired private AuthenticationRequestParamValidator authRequestParamValidator;
  @Autowired private ApplicationEntityRepository applicationRepository;
  @Autowired private TenantEntityRepository tenantRepository;
  @Autowired private AuthorizationCodeEntityInstantiator authCodeInstantiator;
  @Autowired private AuthorizationCodeEntityRepository authCodeEntityRepository;

  @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = NotAuthorizedException.class)
  public AuthenticationResponse authenticate(
      final AuthenticationRequest request, final Map<String, String> params) {
    authenticationRequestValidator.validate(request);
    authRequestParamValidator.validate(params);

    var application =
        applicationRepository
            .findByClientId(request.getClientId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "application not found for client id: " + request.getClientId()));
    var emailOptional =
        emailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            request.getUsername(), application.getTenantId());
    if (emailOptional.isPresent()) {
      var identity = emailOptional.get().getIdentity();
      if (identity.isNotLocked()
          && encoder.matches(request.getPassword(), identity.getEncodedPassword())) {

        var tokens = tokenManager.getOrGenerateToken(identity);
        boolean isAuthorized = false;

        for (ApplicationEntity authorizedApplication : identity.getAuthorizedApplications()) {
          if (application.equals(authorizedApplication)) {
            isAuthorized = true;
            break;
          }
        }

        if (identity.doesNotRequirePasswordChange()) {
          final var tenant =
              tenantRepository
                  .findById(application.getTenantId())
                  .orElseThrow(
                      () ->
                          new ResourceNotFoundException(
                              "Tenant not found for id: " + application.getTenantId()));
          if (isAuthorized) {
            final var authCode = authCodeInstantiator.instantiate(identity, params);
            authCodeEntityRepository.save(authCode);
            identity.setLastSuccessfulLogin(new Date());
            identity.setFailedLoginAttempts(0);
            identityRepository.save(identity);
            return AuthenticationResponse.of(
                tokens.getLeft(),
                tokens.getRight(),
                isAuthorized,
                application.getName(),
                tenant.getWebsiteUrl(),
                application.getHomePageUrl(),
                application.getRedirectUri(),
                authCode.getAuthorizationCode());
          }
          identity.setLastSuccessfulLogin(new Date());
          identity.setFailedLoginAttempts(0);
          identityRepository.save(identity);
          return AuthenticationResponse.of(
              tokens.getLeft(),
              tokens.getRight(),
              isAuthorized,
              application.getName(),
              tenant.getWebsiteUrl(),
              application.getHomePageUrl(),
              application.getRedirectUri(),
              null);
        }
      }
      identity.setFailedLoginAttempts(identity.getFailedLoginAttempts() + 1);
      identityRepository.save(identity);
    }
    throw new NotAuthorizedException();
  }
}
