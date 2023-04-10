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
package software.iridium.api.service;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.repository.AuthenticationEntityRepository;

@Component
public class TokenManager {

  @Resource private AuthenticationEntityRepository authenticationEntityRepository;
  @Resource private AuthenticationGenerator authenticationGenerator;

  @Transactional(propagation = Propagation.REQUIRED)
  public ImmutablePair<String, String> getOrGenerateToken(IdentityEntity identityEntity) {
    final var authenticationOptional =
        authenticationEntityRepository.findFirstByIdentityIdOrderByCreatedDesc(
            identityEntity.getId());

    if (authenticationOptional.isEmpty()
        || new Date().after(authenticationOptional.get().getExpiration())) {
      final var generatedAuthentication =
          authenticationGenerator.generateAuthentication(identityEntity);
      authenticationEntityRepository.save(generatedAuthentication);
      return new ImmutablePair<>(
          generatedAuthentication.getAuthToken(), generatedAuthentication.getRefreshToken());
    }

    return new ImmutablePair<>(
        authenticationOptional.get().getAuthToken(),
        authenticationOptional.get().getRefreshToken());
  }
}
