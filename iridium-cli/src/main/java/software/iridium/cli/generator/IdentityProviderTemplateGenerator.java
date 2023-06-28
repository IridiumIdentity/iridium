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
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import software.iridium.cli.util.YamlParser;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;

public class IdentityProviderTemplateGenerator extends AbstractGenerator {

  public static List<ExternalIdentityProviderTemplateEntity> generateGithubProviderTemplates(
      final EntityManager entityManager) throws IOException {
    beginTransaction(entityManager);
    final var externalProviderTemplates =
        YamlParser.readValue(
            "external-provider-templates.yaml",
            new TypeReference<ArrayList<ExternalIdentityProviderTemplateEntity>>() {});
    for (ExternalIdentityProviderTemplateEntity externalProviderTemplate :
        externalProviderTemplates) {
      entityManager.persist(externalProviderTemplate);
    }

    flushAndCommitTransaction(entityManager);
    return externalProviderTemplates;
  }
}
