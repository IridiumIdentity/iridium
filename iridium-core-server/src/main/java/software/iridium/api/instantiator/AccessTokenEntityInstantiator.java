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
import software.iridium.api.entity.AccessTokenEntity;
import software.iridium.api.util.DateUtils;
import software.iridium.api.util.TokenGenerator;

@Component
public class AccessTokenEntityInstantiator {

  private static final Integer HOURS_TO_EXPIRATION = 1;

  @Resource private TokenGenerator tokenGenerator;

  @Transactional(propagation = Propagation.REQUIRED)
  public AccessTokenEntity instantiate(final String identityId) {
    final var entity = new AccessTokenEntity();
    entity.setIdentityId(identityId);
    entity.setExpiration(DateUtils.addHoursToCurrentTime(1));
    entity.setTokenType("Bearer");
    final var accessToken =
        tokenGenerator.generateAccessToken(
            identityId, DateUtils.addHoursToCurrentTime(HOURS_TO_EXPIRATION));
    entity.setAccessToken(accessToken);
    // final var refreshToken = refreshTokenInstantiator.instantiate(entity);
    return entity;
  }
}
