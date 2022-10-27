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

import java.util.Date;
import javax.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.iridium.api.entity.EmailVerificationTokenEntity;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.util.DateUtils;

public class EmailVerificationTokenEntityInstantiator {

  @Resource private BCryptPasswordEncoder encoder;

  public EmailVerificationTokenEntity createEmailVerificationToken(IdentityEmailEntity email) {
    final var emailVerificationToken = new EmailVerificationTokenEntity();
    emailVerificationToken.setToken(encoder.encode(new Date() + email.getIdentity().getId()));
    emailVerificationToken.setExpiration(DateUtils.addHoursToCurrentTime(2));
    emailVerificationToken.setEmail(email);
    return emailVerificationToken;
  }
}
