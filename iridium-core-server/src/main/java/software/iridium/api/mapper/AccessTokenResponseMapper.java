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
import software.iridium.api.authentication.domain.AccessTokenResponse;
import software.iridium.api.entity.AccessTokenEntity;

@Component
public class AccessTokenResponseMapper {

  public AccessTokenResponse map(final AccessTokenEntity entity) {
    final var response = new AccessTokenResponse();
    response.setAccessToken(entity.getAccessToken());
    // response.setRefreshToken(entity.getRefreshToken().getRefreshToken());
    response.setTokenType(entity.getTokenType());
    response.setExpiresIn(3600L);
    return response;
  }
}
