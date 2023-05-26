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

import jakarta.persistence.EntityManager;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.entity.TenantEntity;

public class IdentityProviderGenerator extends AbstractGenerator {

  public static ExternalIdentityProviderEntity generateIdentityProvider(
      final EntityManager entityManager,
      final ExternalIdentityProviderTemplateEntity identityProviderTemplate,
      final TenantEntity iridiumTenant,
      final String githubClientId,
      final String githubClientSecret) {
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
    identityProvider.setRedirectUri("http://localhost:4200/dashboard");
    identityProvider.setBaseAuthorizationUrl(identityProviderTemplate.getBaseAuthorizationUrl());
    entityManager.persist(identityProvider);
    flushAndCommitTransaction(entityManager);
    return identityProvider;
  }
}
