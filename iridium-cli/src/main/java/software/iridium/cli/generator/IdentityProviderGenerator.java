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
import java.util.List;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.entity.TenantEntity;

public class IdentityProviderGenerator extends AbstractGenerator {

  public static List<ExternalIdentityProviderEntity> generateIdentityProvider(
      final EntityManager entityManager,
      final List<ExternalIdentityProviderTemplateEntity> identityProviderTemplates,
      final TenantEntity iridiumTenant,
      final ObjectMapper objectMapper,
      final String confPath)
      throws IOException {
    beginTransaction(entityManager);
    final List<ExternalIdentityProviderEntity> externalProviders =
        objectMapper.readValue(
            new File(confPath + "external-providers.yaml"),
            new TypeReference<List<ExternalIdentityProviderEntity>>() {});
    for (ExternalIdentityProviderTemplateEntity externalProviderTemplate :
        identityProviderTemplates) {
      for (ExternalIdentityProviderEntity externalProvider : externalProviders) {
        if (externalProviderTemplate.getName().equals(externalProvider.getName())) {
          externalProvider.setTemplate(externalProviderTemplate);
          externalProvider.setIconPath(externalProviderTemplate.getIconPath());
          entityManager.persist(externalProvider);
        }
      }
    }

    flushAndCommitTransaction(entityManager);
    return externalProviders;
  }
}
