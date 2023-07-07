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
package software.iridium.api.instantiator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderRequest;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.entity.TenantEntity;

@Component
public class ExternalIdentityProviderInstantiator {

  @Autowired private ParameterHashMapInstantiator paramHashMapInstantiator;

  @Transactional(propagation = Propagation.REQUIRED)
  public ExternalIdentityProviderEntity instantiate(
      final TenantEntity tenant,
      final CreateExternalIdentityProviderRequest request,
      final ExternalIdentityProviderTemplateEntity template) {
    final var entity = new ExternalIdentityProviderEntity();
    entity.setTemplate(template);
    entity.setName(template.getName());
    entity.setTenant(tenant);
    entity.setAuthorizationParameters(
        paramHashMapInstantiator.instantiate(template.getAuthorizationParameters()));
    entity.setAccessTokenParameters(
        paramHashMapInstantiator.instantiate(template.getAccessTokenParameters()));
    entity.setBaseAuthorizationUrl(template.getBaseAuthorizationUrl());
    entity.setProfileRequestBaseUrl(template.getProfileRequestBaseUrl());
    entity.setAccessTokenRequestBaseUrl(template.getAccessTokenRequestBaseUrl());
    entity.setClientSecret(request.getClientSecret());
    entity.setClientId(request.getClientId());
    entity.setScope(template.getDefaultScope());
    entity.setIconPath(template.getIconPath());
    return entity;
  }
}
