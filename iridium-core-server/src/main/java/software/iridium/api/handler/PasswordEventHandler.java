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
package software.iridium.api.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.instantiator.SelfUrlGenerator;
import software.iridium.api.service.EmailService;

@Component
public class PasswordEventHandler {

  @Autowired private EmailSendRequestInstantiator emailInstantiator;
  @Autowired private SelfUrlGenerator urlGenerator;
  @Autowired private EmailService emailService;

  @Transactional(propagation = Propagation.REQUIRED)
  public void handlePasswordResetInitiatedEvent(
      final IdentityEntity identity, final String clientId) {
    Map<String, Object> props = new HashMap<>();
    props.put(
        "forgotPasswordLink",
        urlGenerator.generateChangePasswordUrl(identity.getPasswordResetToken(), clientId));
  }

  public void handlePasswordResetEvent(final IdentityEntity identity) {
    final var primaryEmail = identity.getPrimaryEmail();

    Map<String, Object> props = new HashMap<>();
    props.put("tenantName", "IridiumID");
    props.put("tenantHelpEmail", "help@iridium.software");

    emailService.send(
        emailInstantiator.instantiate(
            primaryEmail, "Iridium Password Change Notification", props, "confirm-password-reset"));
  }
}
