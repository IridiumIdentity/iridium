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
import javax.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.InitiatePasswordResetRequest;
import software.iridium.api.authentication.domain.PasswordResetRequest;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.handler.PasswordEventHandler;
import software.iridium.api.instantiator.PasswordResetTokenEntityInstantiator;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.IdentityEmailEntityRepository;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.repository.PasswordResetTokenEntityRepository;

@Service
public class PasswordService {

  @Resource private IdentityEntityRepository identityRepository;
  @Resource private PasswordResetTokenEntityInstantiator tokenInstantiator;
  @Resource private IdentityEmailEntityRepository emailRepository;
  @Resource private PasswordEventHandler passwordEventHandler;
  @Resource private PasswordResetTokenEntityRepository resetTokenRepository;
  @Resource private ApplicationEntityRepository applicationRepository;

  @Transactional(propagation = Propagation.REQUIRED)
  public Boolean initiatePasswordReset(final InitiatePasswordResetRequest request) {
    // todo: (josh fischer) validate request properties
    applicationRepository
        .findByClientId(request.getClientId())
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "application not found for clientId: " + request.getClientId()));
    final var emailOpt = emailRepository.findByEmailAddress(request.getUsername());
    if (emailOpt.isPresent()) {
      final var identity = emailOpt.get().getIdentity();
      final var token = tokenInstantiator.instantiate(identity);
      identity.setPasswordResetToken(token);
      passwordEventHandler.handlePasswordResetInitiatedEvent(identity, request.getClientId());
      return true;
    } else {
      return false;
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public String resetPassword(final PasswordResetRequest request) {
    // todo (josh fischer) validate request properties
    final var application =
        applicationRepository
            .findByClientId(request.getClientId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "application  not found for clientId: " + request.getClientId()));

    final var resetToken =
        resetTokenRepository
            .findByToken(request.getToken())
            .orElseThrow(
                () -> new ResourceNotFoundException("token does not exist: " + request.getToken()));

    if (resetToken.getExpiration().before(new Date())) {
      throw new IllegalStateException("token expired: ");
    }
    final var identity = resetToken.getIdentity();
    boolean isValidResetRequest = false;
    for (ApplicationEntity authorizedApplication : identity.getAuthorizedApplications()) {
      if (authorizedApplication.equals(application)) {
        isValidResetRequest = true;
        break;
      }
    }
    if (isValidResetRequest) {
      String newPlainTextPassword = request.getNewPassword();
      String encryptedPassword = BCrypt.hashpw(newPlainTextPassword, BCrypt.gensalt());
      identity.setEncodedPassword(encryptedPassword);
      identity.setPasswordResetToken(null);
      identity.setRequiresPasswordChange(false);
      passwordEventHandler.handlePasswordResetEvent(identity);
      final var sessionDetails = identity.getCreateSessionDetails();
      return "/login?client_id="
          + application.getClientId()
          + "&state="
          + sessionDetails.getState()
          + "response_type="
          + sessionDetails.getResponseType()
          + "code_challenge_method="
          + sessionDetails.getCodeChallengeMethod()
          + "code_challenge="
          + sessionDetails.getCodeChallenge()
          + "client_id="
          + sessionDetails.getClientId();
    }
    return "/error";
  }
}
