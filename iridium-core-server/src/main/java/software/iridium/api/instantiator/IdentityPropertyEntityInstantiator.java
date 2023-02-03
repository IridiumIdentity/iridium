/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.IdentityPropertyEntity;

@Component
public class IdentityPropertyEntityInstantiator {

  public static final String GITHUB_AVATAR_URL_KEY = "avatarUrl";
  public static final String GITHUB_LOGIN_KEY = "login";

  @Transactional(propagation = Propagation.REQUIRED)
  public void instantiateGithubProperties(
      final GithubProfileResponse response, final IdentityEntity identity) {
    final var avatarUrlProperty = new IdentityPropertyEntity();

    // todo: think about how we can do this better
    avatarUrlProperty.setIdentity(identity);
    avatarUrlProperty.setName(GITHUB_AVATAR_URL_KEY);
    avatarUrlProperty.setValue(response.getAvatarUrl());
    identity.getIdentityProperties().add(avatarUrlProperty);

    final var loginProperty = new IdentityPropertyEntity();
    loginProperty.setIdentity(identity);
    loginProperty.setValue(response.getLogin());
    loginProperty.setName(GITHUB_LOGIN_KEY);
    identity.getIdentityProperties().add(loginProperty);
  }
}
