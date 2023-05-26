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
import software.iridium.api.authentication.domain.Environment;
import software.iridium.entity.TenantEntity;

public class TenantGenerator extends AbstractGenerator {

  public static TenantEntity generateTenant(final EntityManager entityManager) {
    beginTransaction(entityManager);
    final var iridiumTenant = new TenantEntity();
    iridiumTenant.setEnvironment(Environment.PRODUCTION);
    iridiumTenant.setSubdomain("localhost");
    iridiumTenant.setWebsiteUrl("iridium.software");
    entityManager.persist(iridiumTenant);
    flushAndCommitTransaction(entityManager);
    return iridiumTenant;
  }
}
