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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.iridium.entity.ApplicationTypeEntity;

public class ApplicationTypeGenerator extends AbstractGenerator {
  private static final Logger logger = LoggerFactory.getLogger(ApplicationTypeGenerator.class);

  public static ArrayList<ApplicationTypeEntity> generateApplicationTypes(
      final EntityManager entityManager, final ObjectMapper objectMapper, final String confPath)
      throws IOException {
    logger.info("generating application types");

    final var applicationTypes =
        objectMapper.readValue(
            new File(confPath + "application-types.yaml"),
            new TypeReference<ArrayList<ApplicationTypeEntity>>() {});

    beginTransaction(entityManager);

    applicationTypes.forEach(entityManager::persist);

    flushAndCommitTransaction(entityManager);
    return applicationTypes;
  }
}
