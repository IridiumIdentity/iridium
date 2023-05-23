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
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "init", description = "inits the system")
public class InitCommand implements Runnable {

  private static final Logger logger = LoggerFactory.getLogger(InitCommand.class);

  @Option(
      names = {"-h", "--host"},
      description = "localhost, your-domain.xyz, ...",
      interactive = true)
  private String host;

  @Option(
      names = {"-p", "--password"},
      description = "the database password",
      interactive = true)
  private String password;

  @Option(
      names = {"-u", "--user"},
      description = "the database user",
      interactive = true)
  private String user;

  @Option(
      names = {"-P", "--port"},
      description = "the database port",
      interactive = true)
  private String port;

  @Override
  public void run() {
    if (host == null && System.console() != null) {

      host = System.console().readLine("Enter value for --host: ");
    }

    if (user == null && System.console() != null) {

      user = System.console().readLine("Enter value for --user: ");
    }

    if (password == null && System.console() != null) {

      password = System.console().readLine("Enter value for --password: ");
    }

    if (port == null && System.console() != null) {

      password = System.console().readLine("Enter value for --port: ");
    }
    System.out.println("You provided value '" + host + "'");

    Map<String, String> addedOrOverridenProperties = new HashMap<>();
    addedOrOverridenProperties.put(
        "javax.persistence.jdbc.url", "jdbc:mysql://" + host + ":" + port + "/identities");
    addedOrOverridenProperties.put("javax.persistence.jdbc.user", user);
    addedOrOverridenProperties.put("javax.persistence.jdbc.password", password);
    EntityManager entityManager = null;
    try (var entityManagerFactory =
        Persistence.createEntityManagerFactory("maria-db", addedOrOverridenProperties)) {
      entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();

    } catch (Exception e) {
      logger.error("exception occurred during initialization: ", e);
    } finally {
      if (entityManager != null) {
        entityManager.close();
      }
    }
  }
}
