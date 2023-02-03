/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

  @Resource private BCryptPasswordEncoder encoder;

  public String generateAccessToken(final String identityId, final Date expiration) {
    var formattedExpiration = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S").format(expiration);
    var tokenSource =
        new StringBuilder()
            .append(identityId)
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
