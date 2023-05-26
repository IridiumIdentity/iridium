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
import software.iridium.entity.LoginDescriptorEntity;
import software.iridium.entity.TenantEntity;

public class LoginDescriptorGenerator extends AbstractGenerator {

  public static LoginDescriptorEntity generateLoginDescriptor(
      final EntityManager entityManager, final TenantEntity iridiumTenant) {
    beginTransaction(entityManager);
    final var loginDescriptor = new LoginDescriptorEntity();
    iridiumTenant.setLoginDescriptor(loginDescriptor);
    loginDescriptor.setTenant(iridiumTenant);
    loginDescriptor.setDisplayName("Iridium");
    loginDescriptor.setUsernameErrorHint("Please enter a valid email");
    loginDescriptor.setUsernameLabel("Please enter a valid email");
    loginDescriptor.setUsernamePlaceholder("ex: you@somewhere.com");
    loginDescriptor.setUsernameType("email");
    loginDescriptor.setAllowGithub(false);
    entityManager.persist(loginDescriptor);
    flushAndCommitTransaction(entityManager);
    return loginDescriptor;
  }
}
