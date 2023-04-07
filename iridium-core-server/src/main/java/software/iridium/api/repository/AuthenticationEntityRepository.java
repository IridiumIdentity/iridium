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

import java.util.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import software.iridium.api.entity.AuthenticationEntity;

public interface AuthenticationEntityRepository
    extends JpaRepository<AuthenticationEntity, String> {

  Optional<AuthenticationEntity> findFirstByIdentityIdOrderByCreatedDesc(final String userId);

  Optional<AuthenticationEntity> findFirstByAuthTokenAndExpirationAfter(
      final String token, final Date now);
}
