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

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.iridium.cli.domain.PersistenceProperties;
import software.iridium.cli.util.YamlParser;

public class PersistencePropertyGenerator extends AbstractGenerator {
  private static final Logger logger = LoggerFactory.getLogger(PersistencePropertyGenerator.class);

  public static Map<String, String> generatePersistenceProperties() {
    final var properties = YamlParser.readValue("persistence.yaml", PersistenceProperties.class);
    logger.info("creating persistence properties...");

    Map<String, String> addedOrOverridenProperties = new HashMap<>();
    addedOrOverridenProperties.put(
        "jakarta.persistence.jdbc.url",
        "jdbc:mysql://" + properties.getHost() + ":" + properties.getPort() + "/identities");
    addedOrOverridenProperties.put("jakarta.persistence.jdbc.user", properties.getUsername());
    addedOrOverridenProperties.put("jakarta.persistence.jdbc.password", properties.getPassword());
    return addedOrOverridenProperties;
  }
}
