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
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.iridium.api.email.client.EmailApiClient;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;

@Component
public class NewIdentityEventHandler {

  @Resource private EmailApiClient emailApiClient;
  @Resource private EmailSendRequestInstantiator emailSendRequestInstantiator;

  @Value("${software.iridium.emailNotification.client.baseUrl}")
  private String verifyEmailLink;
  // todo: test me
  public void handleEvent(final IdentityEntity identity, final String clientId) {
    Map<String, Object> props = new HashMap<>();
    final var primaryEmail = identity.getPrimaryEmail();
    props.put(
        "verifyEmailLink",
        verifyEmailLink + "login?id=" + primaryEmail.getEmailAddress() + "&client_id=" + clientId);
    emailApiClient.sendNewIdentityVerificationMail(
        emailSendRequestInstantiator.instantiate(
            primaryEmail, "Iridium Email Verification", props, "new-identity"));
  }
}
