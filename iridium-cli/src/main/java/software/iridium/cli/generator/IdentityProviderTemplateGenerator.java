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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.iridium.cli.util.YamlParser;
import software.iridium.entity.ExternalIdentityProviderParameterTemplateEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;

public class IdentityProviderTemplateGenerator extends AbstractGenerator {

  private static final Logger logger =
      LoggerFactory.getLogger(IdentityProviderTemplateGenerator.class);

  public static List<ExternalIdentityProviderTemplateEntity> generate(
      final EntityManager entityManager) throws IOException {
    beginTransaction(entityManager);
    final var externalProviderTemplates =
        YamlParser.readValue(
            "external-provider-templates.yaml",
            new TypeReference<ArrayList<ExternalIdentityProviderTemplateEntity>>() {});
    for (ExternalIdentityProviderTemplateEntity externalProviderTemplate :
        externalProviderTemplates) {
      logger.info(
          "parameter size: " + externalProviderTemplate.getAuthorizationParameters().size());
      for (ExternalIdentityProviderParameterTemplateEntity authorizationParam :
          externalProviderTemplate.getAuthorizationParameters()) {
        authorizationParam.setProvider(externalProviderTemplate);
      }
      for (ExternalIdentityProviderParameterTemplateEntity accessTokenParams :
          externalProviderTemplate.getAccessTokenParameters()) {
        accessTokenParams.setProvider(externalProviderTemplate);
      }
      entityManager.persist(externalProviderTemplate);
    }

    flushAndCommitTransaction(entityManager);
    return externalProviderTemplates;
  }
}
