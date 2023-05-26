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
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ApplicationTypeEntity;
import software.iridium.entity.TenantEntity;

public class ApplicationGenerator extends AbstractGenerator {

  public static ApplicationEntity generateIridiumApplication(
      final EntityManager entityManager,
      final TenantEntity iridiumTenant,
      final ApplicationTypeEntity applicationType) {
    beginTransaction(entityManager);
    final var iridiumManagementApp = new ApplicationEntity();
    iridiumManagementApp.setHomePageUrl("http://localhost:4200");
    iridiumManagementApp.setName("iridium management app");
    iridiumManagementApp.setRedirectUri("http://localhost:4200/dashboard");
    iridiumManagementApp.setTenantId(iridiumTenant.getId());
    iridiumManagementApp.setClientId("xd4rtddkthdfh234r");
    iridiumManagementApp.setApplicationType(applicationType);
    entityManager.persist(iridiumManagementApp);
    flushAndCommitTransaction(entityManager);
    return iridiumManagementApp;
  }
}
