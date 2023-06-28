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
package software.iridium.cli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import software.iridium.cli.generator.*;
import software.iridium.cli.util.PathUtils;
import software.iridium.entity.*;

@Command(name = "init", description = "inits the system")
public class InitCommand implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(InitCommand.class);

  private String iridiumAppId;

  @Override
  public void run() {
    final var configurationPath = PathUtils.getJarPath();
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    final Map<String, String> properties;

    try {
      properties =
          PersistencePropertyGenerator.generatePersistenceProperties(
              objectMapper, configurationPath);
    } catch (IOException e) {
      throw new RuntimeException("Error reading persistence properties", e);
    }
    try (var entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager()) {

      final var applicationTypes =
          ApplicationTypeGenerator.generateApplicationTypes(
              entityManager, objectMapper, configurationPath);

      final TenantEntity iridiumTenant =
          TenantGenerator.generateTenant(entityManager, objectMapper, configurationPath);

      LoginDescriptorGenerator.generateLoginDescriptor(
          entityManager, iridiumTenant, objectMapper, configurationPath);

      IdentityProviderGenerator.generateIdentityProvider(
          entityManager,
          IdentityProviderTemplateGenerator.generateGithubProviderTemplate(
              entityManager, objectMapper, configurationPath),
          iridiumTenant,
          objectMapper,
          configurationPath);

      for (ApplicationTypeEntity typeEntity : applicationTypes) {
        if (typeEntity.getType().equals(ApplicationType.SINGLE_PAGE)) {
          final var application =
              ApplicationGenerator.generateIridiumApplication(
                  entityManager, iridiumTenant, typeEntity, objectMapper, configurationPath);
          this.iridiumAppId = application.getClientId();
        }
      }

    } catch (Exception e) {
      logger.info("########################################");
      logger.error("exception occurred during initialization: ", e);
      logger.info("########################################");
      return;
    }
    logger.info("########################################");
    logger.info("Database successfully initialized");
    logger.info("########################################");
    logger.info("Iridium Management Application ID is " + iridiumAppId);
    logger.info("Be sure to place this in the appropriate environment.ts file");
  }
}
