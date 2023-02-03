/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
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
