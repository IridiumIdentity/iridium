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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.service.EmailService;
import software.iridium.entity.IdentityEntity;

@Component
public class NewIdentityEventHandler {
  @Autowired private EmailSendRequestInstantiator emailSendRequestInstantiator;

  @Autowired private EmailService emailService;

  @Value("${software.iridium.emailNotification.client.baseUrl}")
  private String verifyEmailLink;

  public void handleEvent(final IdentityEntity identity, final String clientId) {
    Map<String, Object> props = new HashMap<>();
    final var primaryEmail = identity.getPrimaryEmail();
    props.put(
        "verifyEmailLink",
        verifyEmailLink + "login?id=" + primaryEmail.getEmailAddress() + "&client_id=" + clientId);
    final var emailRequest = new EmailSendRequest();
    emailService.send(
        emailSendRequestInstantiator.instantiate(
            primaryEmail, "Iridium Email Verification", props, "new-identity"));
  }
}
