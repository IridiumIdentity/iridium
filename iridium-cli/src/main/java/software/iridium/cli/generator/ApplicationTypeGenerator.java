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
import java.util.ArrayList;
import software.iridium.entity.ApplicationType;
import software.iridium.entity.ApplicationTypeEntity;

public class ApplicationTypeGenerator extends AbstractGenerator {

  public static ArrayList<ApplicationTypeEntity> generateApplicationTypes(
      final EntityManager entityManager) {
    beginTransaction(entityManager);
    // create application_types
    final var singlePageApplicationType = new ApplicationTypeEntity();
    singlePageApplicationType.setName("Single Page Application");
    singlePageApplicationType.setDescription(
        "Applications running in the browser.  Angular, React, Vue, etc.");
    singlePageApplicationType.setType(ApplicationType.SINGLE_PAGE);
    singlePageApplicationType.setRequiresSecret(false);

    entityManager.persist(singlePageApplicationType);

    final var webServiceApplicationType = new ApplicationTypeEntity();
    webServiceApplicationType.setName("Web Service Application");
    webServiceApplicationType.setDescription(
        "A regular web app running on a remote server. Spring, Ruby on Rails, etc.");
    webServiceApplicationType.setType(ApplicationType.WEB_SERVICE);
    webServiceApplicationType.setRequiresSecret(true);

    final var applicationTypes = new ArrayList<ApplicationTypeEntity>();
    applicationTypes.add(singlePageApplicationType);
    applicationTypes.add(webServiceApplicationType);
    entityManager.persist(webServiceApplicationType);
    flushAndCommitTransaction(entityManager);
    return applicationTypes;
  }
}
