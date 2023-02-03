/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.ProfileResponse;
import software.iridium.api.entity.ProfileEntity;

@Component
public class ProfileResponseMapper {

  public ProfileResponse map(final ProfileEntity profile) {
    if (profile == null) {
      return null;
    }
    final var response = new ProfileResponse();
    response.setFirstName(profile.getFirstName());
    response.setLastName(profile.getLastName());
    return response;
  }
}
