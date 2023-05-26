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
package software.iridium.api.instantiator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ClientSecretEntity;

@Component
public class ClientSecretEntityInstantiator {

  private static final Logger logger =
      LoggerFactory.getLogger(ClientSecretEntityInstantiator.class);

  @Autowired private BCryptPasswordEncoder encoder;

  @Transactional(propagation = Propagation.REQUIRED)
  public ClientSecretEntity instantiate(final ApplicationEntity application, final String secret) {
    logger.info("generating secret for: " + application.getId());
    final var entity = new ClientSecretEntity();
    entity.setApplication(application);
    entity.setSecretKey(encoder.encode(secret));
    application.getClientSecrets().add(entity);
    return entity;
  }
}
