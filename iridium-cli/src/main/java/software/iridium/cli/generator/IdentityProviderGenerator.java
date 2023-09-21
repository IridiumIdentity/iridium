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
import java.util.HashMap;
import java.util.List;
import software.iridium.cli.util.YamlParser;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.ExternalIdentityProviderParameterTemplateEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.entity.TenantEntity;

public class IdentityProviderGenerator extends AbstractGenerator {

  public static List<ExternalIdentityProviderEntity> generate(
      final EntityManager entityManager,
      final List<ExternalIdentityProviderTemplateEntity> templates,
      final TenantEntity iridiumTenant) {
    beginTransaction(entityManager);
    final List<ExternalIdentityProviderEntity> externalProviders =
        YamlParser.readValue("external-providers.yaml", new TypeReference<>() {});
    for (ExternalIdentityProviderTemplateEntity template : templates) {
      for (ExternalIdentityProviderEntity externalProvider : externalProviders) {
        if (template.getName().equals(externalProvider.getName())) {
          externalProvider.setTemplate(template);
          externalProvider.setAccessTokenRequestBaseUrl(template.getAccessTokenRequestBaseUrl());
          externalProvider.setProfileRequestBaseUrl(template.getProfileRequestBaseUrl());
          externalProvider.setIconPath(template.getIconPath());
          externalProvider.setDisplayName(template.getDisplayName());
          externalProvider.setTenant(iridiumTenant);
          externalProvider.setBaseAuthorizationUrl(template.getBaseAuthorizationUrl());

          final var authorizationParamHashMap = new HashMap<String, String>();
          for (ExternalIdentityProviderParameterTemplateEntity authorizationParam :
              template.getAuthorizationParameters()) {
            authorizationParamHashMap.put(
                authorizationParam.getName(), authorizationParam.getValue());
          }
          externalProvider.setAuthorizationParameters(authorizationParamHashMap);

          final var accessTokenParamHashMap = new HashMap<String, String>();
          for (ExternalIdentityProviderParameterTemplateEntity accessTokenParam :
              template.getAccessTokenParameters()) {
            accessTokenParamHashMap.put(accessTokenParam.getName(), accessTokenParam.getValue());
          }
          externalProvider.setAccessTokenParameters(accessTokenParamHashMap);
          externalProvider.setScope(template.getDefaultScope());
          entityManager.persist(externalProvider);
        }
      }
    }

    flushAndCommitTransaction(entityManager);
    return externalProviders;
  }
}
