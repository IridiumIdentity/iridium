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

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.entity.ExternalIdentityProviderEntity;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;

@Component
public class IdentityEntityInstantiator {

  @Resource private EmailEntityInstantiator emailInstantiator;
  @Resource private IdentityPropertyEntityInstantiator propertyInstantiator;

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityEntity instantiate(
      final CreateIdentityRequest request,
      final String encodedTempPassword,
      final String tenantId) {
    final IdentityEntity entity = instantiateIdentityAndAssociate(request.getUsername());
    entity.setEncodedPassword(encodedTempPassword);
    entity.setParentTenantId(tenantId);
    return entity;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityEntity instantiateFromGithub(
      final GithubProfileResponse response, final ExternalIdentityProviderEntity provider) {
    final IdentityEntity identity = instantiateIdentityAndAssociate(response.getEmail());
    identity.setProvider(provider);
    identity.setExternalId(response.getId());
    propertyInstantiator.instantiateGithubProperties(response, identity);
    return identity;
  }

  private IdentityEntity instantiateIdentityAndAssociate(final String response) {
    final var identity = new IdentityEntity();
    final var email = emailInstantiator.instantiatePrimaryEmail(response);
    associate(identity, email);
    return identity;
  }

  private void associate(final IdentityEntity entity, final IdentityEmailEntity email) {
    entity.getEmails().add(email);
    email.setIdentity(entity);
  }
}
