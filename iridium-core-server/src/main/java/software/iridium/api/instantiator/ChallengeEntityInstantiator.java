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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.util.DateUtils;
import software.iridium.entity.ChallengeEntity;
import software.iridium.entity.ChallengeType;
import software.iridium.entity.TenantEntity;

@Component
public class ChallengeEntityInstantiator {

  private static final String chars =
      "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int length = 23;
  private static final int registrationWindow = 10;

  @Transactional(propagation = Propagation.REQUIRED)
  public ChallengeEntity instantiate(final TenantEntity tenant, final ChallengeType type) {
    try {
      final var challenge = new ChallengeEntity();
      challenge.setTenant(tenant);
      final var secureRandom = SecureRandom.getInstanceStrong();
      final String secureChallenge =
          secureRandom
              .ints(length, 0, chars.length())
              .mapToObj(chars::charAt)
              .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
              .toString();
      challenge.setChallenge(secureChallenge);
      challenge.setType(type);
      challenge.setExpiration(DateUtils.addMinutesToCurrentTime(registrationWindow));
      return challenge;

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error creating challenge ", e);
    }
  }
}
