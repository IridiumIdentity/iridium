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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import software.iridium.api.entity.AuthenticationEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.util.DateUtils;

@Component
public class AuthenticationGenerator {

  @Resource private BCryptPasswordEncoder encoder;

  @Value("${software.iridium.api.ttl.minutes}")
  private Integer tokenTimeToLiveInMinutes;

  public AuthenticationEntity generateAuthentication(final IdentityEntity identityEntity) {
    final var authEntity = new AuthenticationEntity();
    authEntity.setIdentity(identityEntity);
    final var expiration = DateUtils.addHoursToCurrentTime(tokenTimeToLiveInMinutes);
    authEntity.setExpiration(expiration);
    authEntity.setAuthToken(generateAuthToken(identityEntity, expiration));
    authEntity.setRefreshToken(generateRefreshToken(authEntity.getAuthToken()));
    return authEntity;
  }

  public String generateAuthToken(final IdentityEntity identityEntity, final Date expiration) {
    var formattedExpiration = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S").format(expiration);
    var tokenSource =
        new StringBuilder()
            .append(identityEntity.getPrimaryEmail().getEmailAddress())
            .append("||")
            .append(formattedExpiration)
            .append("||")
            .append(UUID.randomUUID())
            .toString();
    return encoder.encode(tokenSource);
  }

  public String generateRefreshToken(final String authToken) {
    return encoder.encode(authToken);
  }
}
