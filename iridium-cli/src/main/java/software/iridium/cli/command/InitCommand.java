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

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import software.iridium.cli.generator.*;
import software.iridium.entity.*;

@Command(name = "init", description = "inits the system")
public class InitCommand implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(InitCommand.class);

  @Option(
      names = {"-h", "--host"},
      description = "localhost, your-domain.xyz, ...")
  private String host;

  @Option(
      names = {"-p", "--password"},
      description = "the database password",
      interactive = true)
  private char[] password;

  @Option(
      names = {"-x", "--admin-password"},
      description = "the admin password",
      interactive = true)
  private char[] adminPassword;

  @Option(
      names = {"-e", "--admin-email"},
      description = "the admin email")
  private String adminEmail;

  @Option(
      names = {"-u", "--user"},
      description = "the database user")
  private String user;

  @Option(
      names = {"-P", "--port"},
      description = "the database port")
  private String port;

  @Option(
      names = {"-g", "--allow-github"},
      description = "allow github login",
      defaultValue = "false")
  private Boolean allowGithub;

  private String githubClientId;
  private String githubClientSecret;

  private String iridiumAppId;

  @Override
  public void run() {
    if (allowGithub) {
      githubClientId = System.console().readLine("Enter value for github client id: ");
      githubClientSecret = System.console().readLine("Enter value for github client secret: ");
    }

    final var properties =
        PersistenePropertyGenerator.generatePersistenceProperties(host, port, user, password);
    try (var entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager()) {

      final var applicationTypes = ApplicationTypeGenerator.generateApplicationTypes(entityManager);

      final TenantEntity iridiumTenant = TenantGenerator.generateTenant(entityManager);

      LocalIdentityGenerator.generate(
          iridiumTenant, String.valueOf(adminPassword), adminEmail, entityManager);

      LoginDescriptorGenerator.generateLoginDescriptor(entityManager, iridiumTenant);

      if (allowGithub) {

        IdentityProviderGenerator.generateIdentityProvider(
            entityManager,
            IdentityProviderTemplateGenerator.generateGithubProviderTemplate(entityManager),
            iridiumTenant,
            githubClientId,
            githubClientSecret);
      }

      for (ApplicationTypeEntity typeEntity : applicationTypes) {
        if (typeEntity.getType().equals(ApplicationType.SINGLE_PAGE)) {
          final var application =
              ApplicationGenerator.generateIridiumApplication(
                  entityManager, iridiumTenant, typeEntity);
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
