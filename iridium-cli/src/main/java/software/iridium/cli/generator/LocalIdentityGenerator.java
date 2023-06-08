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
package software.iridium.cli.generator;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import software.iridium.entity.IdentityEmailEntity;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.TenantEntity;

public class LocalIdentityGenerator extends AbstractGenerator {

  private static final Logger logger = LoggerFactory.getLogger(LocalIdentityGenerator.class);

  public static IdentityEntity generate(
      final TenantEntity iridiumTenant,
      final String password,
      final String emailAddress,
      final EntityManager entityManager) {
    beginTransaction(entityManager);
    final var identity = new IdentityEntity();
    identity.setParentTenantId(iridiumTenant.getId());
    identity.setEncodedPassword(BCrypt.hashpw(String.valueOf(password), BCrypt.gensalt()));
    IdentityEmailEntity userEmail = new IdentityEmailEntity();
    userEmail.setEmailAddress(emailAddress);
    userEmail.setPrimary(true);
    userEmail.setVerified(true);
    identity.getEmails().add(userEmail);
    userEmail.setIdentity(identity);
    entityManager.persist(identity);
    flushAndCommitTransaction(entityManager);
    return identity;
  }
}
