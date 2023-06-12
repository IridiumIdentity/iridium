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
package software.iridium.api.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import software.iridium.entity.IdentityEntity;

public interface IdentityEntityRepository extends JpaRepository<IdentityEntity, String> {

  Optional<IdentityEntity> findByProvider_IdAndExternalId(
      final String providerId, final String externalId);

  Page<IdentityEntity> findAllByParentTenantIdAndActive(
      String tenantId, Boolean active, PageRequest of);
}
