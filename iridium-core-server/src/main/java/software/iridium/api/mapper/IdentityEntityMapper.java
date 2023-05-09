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
package software.iridium.api.mapper;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.TenantEntity;
import software.iridium.api.entity.RoleEntity;

@Component
public class IdentityEntityMapper {

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityResponse map(final IdentityEntity entity) {
    if (entity == null) {
      return null;
    }

    final var response = new IdentityResponse();
    response.setId(entity.getId());
    response.setUsername(entity.getPrimaryEmail().getEmailAddress());
    for (TenantEntity tenantEntity : entity.getManagedTenants()) {
      response.getTenantIds().add(tenantEntity.getId());
    }
    response
            .getRoles()
            .addAll(entity.getRoles().stream().map(RoleEntity::getName).toList());
    return response;
  }
}
