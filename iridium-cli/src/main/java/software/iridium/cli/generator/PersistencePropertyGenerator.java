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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import software.iridium.cli.domain.PersistenceProperties;

public class PersistencePropertyGenerator extends AbstractGenerator {

  public static Map<String, String> generatePersistenceProperties(
      final ObjectMapper objectMapper, final String confPath) throws IOException {
    final var properties =
        objectMapper.readValue(
            new File(confPath + "persistence.yaml"), PersistenceProperties.class);
    Map<String, String> addedOrOverridenProperties = new HashMap<>();
    addedOrOverridenProperties.put(
        "jakarta.persistence.jdbc.url",
        "jdbc:mysql://" + properties.getHost() + ":" + properties.getPort() + "/identities");
    addedOrOverridenProperties.put("jakarta.persistence.jdbc.user", properties.getUsername());
    addedOrOverridenProperties.put("jakarta.persistence.jdbc.password", properties.getPassword());
    return addedOrOverridenProperties;
  }
}
