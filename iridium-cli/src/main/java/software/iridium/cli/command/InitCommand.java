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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import software.iridium.api.authentication.domain.Environment;
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
      names = {"-u", "--user"},
      description = "the database user")
  private String user;

  @Option(
      names = {"-P", "--port"},
      description = "the database port")
  private String port;

  @Option(
      names = {"-g", "--allow-github"},
      description = "allow github login")
  private Boolean allowGithub;

  private String githubClientId;
  private String githubClientSecret;

  @Override
  public void run() {
    if (allowGithub) {
      githubClientId = System.console().readLine("Enter value for github client id: ");
      githubClientSecret = System.console().readLine("Enter value for github client secret: ");
    }

    final var addedOrOverridenProperties =
        generatePersistenceProperties(host, port, user, password);
    try (var entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence", addedOrOverridenProperties);
        EntityManager entityManager = entityManagerFactory.createEntityManager()) {

      // create external_identity_provider_templates
      final ExternalIdentityProviderTemplateEntity identityProviderTemplate =
          generateGithubProviderTemplate(entityManager);

      generateApplicationTypes(entityManager);

      // create tenant
      final TenantEntity iridiumTenant = generateTenant(entityManager);

      // create login_descriptors
      generateLoginDescriptor(entityManager, iridiumTenant);

      if (allowGithub) {

        generateIdentityProvider(entityManager, identityProviderTemplate, iridiumTenant);
      }

      // create iridium application
      generateIridiumApplication(entityManager, iridiumTenant);

    } catch (Exception e) {
      logger.info("########################################");
      logger.error("exception occurred during initialization: ", e);
      logger.info("########################################");
      return;
    }
    logger.info("########################################");
    logger.info("Database successfully initialized");
    logger.info("########################################");
  }

  private static ApplicationEntity generateIridiumApplication(
      final EntityManager entityManager, final TenantEntity iridiumTenant) {
    beginTransaction(entityManager);
    final var iridiumManagementApp = new ApplicationEntity();
    iridiumManagementApp.setHomePageUrl("http://localhost:4200");
    iridiumManagementApp.setName("iridium management app");
    iridiumManagementApp.setRedirectUri("http://localhost:4200/dashboard");
    iridiumManagementApp.setTenantId(iridiumTenant.getId());
    iridiumManagementApp.setClientId("xd4rtddkthdfh234r");
    entityManager.persist(iridiumManagementApp);
    flushAndCommitTransaction(entityManager);
    return iridiumManagementApp;
  }

  private ExternalIdentityProviderEntity generateIdentityProvider(
      final EntityManager entityManager,
      final ExternalIdentityProviderTemplateEntity identityProviderTemplate,
      final TenantEntity iridiumTenant) {
    beginTransaction(entityManager);
    final var identityProvider = new ExternalIdentityProviderEntity();
    identityProvider.setName(identityProviderTemplate.getName());
    identityProvider.setTemplate(identityProviderTemplate);
    identityProvider.setAccessTokenRequestBaseUrl(
        identityProviderTemplate.getAccessTokenRequestBaseUrl());
    identityProvider.setProfileRequestBaseUrl(identityProviderTemplate.getProfileRequestBaseUrl());
    identityProvider.setClientId(githubClientId);
    identityProvider.setClientSecret(githubClientSecret);
    identityProvider.setTenant(iridiumTenant);
    identityProvider.setIconPath(identityProviderTemplate.getIconPath());
    identityProvider.setScope("user:email");
    identityProvider.setRedirectUri("This shouldn't be used");
    identityProvider.setBaseAuthorizationUrl(identityProviderTemplate.getBaseAuthorizationUrl());
    entityManager.persist(identityProvider);
    flushAndCommitTransaction(entityManager);
    return identityProvider;
  }

  private static LoginDescriptorEntity generateLoginDescriptor(
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
    entityManager.persist(loginDescriptor);
    flushAndCommitTransaction(entityManager);
    return loginDescriptor;
  }

  private static TenantEntity generateTenant(final EntityManager entityManager) {
    beginTransaction(entityManager);
    final var iridiumTenant = new TenantEntity();
    iridiumTenant.setEnvironment(Environment.PRODUCTION);
    iridiumTenant.setSubdomain("iridium");
    iridiumTenant.setWebsiteUrl("iridium.software");
    entityManager.persist(iridiumTenant);
    flushAndCommitTransaction(entityManager);
    return iridiumTenant;
  }

  private static ArrayList<ApplicationTypeEntity> generateApplicationTypes(
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

  private static ExternalIdentityProviderTemplateEntity generateGithubProviderTemplate(
      final EntityManager entityManager) {
    beginTransaction(entityManager);
    final var identityProviderTemplate = new ExternalIdentityProviderTemplateEntity();
    identityProviderTemplate.setAccessTokenRequestBaseUrl(
        "https://github.com/login/oauth/access_token");
    identityProviderTemplate.setProfileRequestBaseUrl("https://api.github.com/user");
    identityProviderTemplate.setName("github");
    identityProviderTemplate.setIconPath("https://avatars.githubusercontent.com/in/15368");
    identityProviderTemplate.setBaseAuthorizationUrl("https://github.com/login/oauth/authorize?");
    entityManager.persist(identityProviderTemplate);
    flushAndCommitTransaction(entityManager);
    return identityProviderTemplate;
  }

  private static Map<String, String> generatePersistenceProperties(
      final String host, final String port, final String user, final char[] password) {
    Map<String, String> addedOrOverridenProperties = new HashMap<>();
    addedOrOverridenProperties.put(
        "jakarta.persistence.jdbc.url", "jdbc:mysql://" + host + ":" + port + "/identities");
    addedOrOverridenProperties.put("jakarta.persistence.jdbc.user", user);
    addedOrOverridenProperties.put("jakarta.persistence.jdbc.password", new String(password));
    return addedOrOverridenProperties;
  }

  private static void beginTransaction(final EntityManager entityManager) {
    entityManager.getTransaction().begin();
  }

  private static void flushAndCommitTransaction(final EntityManager entityManager) {
    entityManager.flush();
    entityManager.getTransaction().commit();
  }
}
