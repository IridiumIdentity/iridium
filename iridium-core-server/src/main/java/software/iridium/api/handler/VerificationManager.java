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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import software.iridium.api.instantiator.EmailVerificationTokenEntityInstantiator;
import software.iridium.entity.*;

@Component
public class VerificationManager {
  private static final Logger logger = LoggerFactory.getLogger(VerificationManager.class);

  @Autowired private EmailVerificationTokenEntityInstantiator instantiator;
  @Autowired private WebClient webClient;

  @Value("${base.server.url}")
  private String baseURL;

  @Transactional(propagation = Propagation.REQUIRED)
  public void handleNewEmailEvent(
      final IdentityEntity identity,
      final ApplicationEntity application,
      final TenantEntity tenant) {
    identity
        .getPrimaryEmail()
        .setEmailVerificationToken(
            instantiator.createEmailVerificationToken(identity.getPrimaryEmail()));
    logger.info(
        "reset token email is {}",
        identity.getPrimaryEmail().getEmailVerificationToken().getToken());
    final var verificationLink =
        "https://"
            + tenant.getSubdomain()
            + baseURL
            + "/verify-email?token="
            + identity.getPrimaryEmail().getEmailVerificationToken().getToken();
  }
}
