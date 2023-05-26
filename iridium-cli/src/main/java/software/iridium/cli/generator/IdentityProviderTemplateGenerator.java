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
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;

public class IdentityProviderTemplateGenerator extends AbstractGenerator {

  public static ExternalIdentityProviderTemplateEntity generateGithubProviderTemplate(
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
}
