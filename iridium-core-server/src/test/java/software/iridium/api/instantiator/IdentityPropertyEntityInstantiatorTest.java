/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.IdentityPropertyEntity;

@ExtendWith(MockitoExtension.class)
class IdentityPropertyEntityInstantiatorTest {

  private IdentityPropertyEntityInstantiator subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new IdentityPropertyEntityInstantiator();
  }

  @Test
  public void instantiateGithubProperties_AllGood_InstantiatesAsExpected() {
    final var avatarUrl = "http://somelocation.com";
    final var login = "username123";
    final var githubResponse = new GithubProfileResponse();
    githubResponse.setAvatarUrl(avatarUrl);
    githubResponse.setLogin(login);
    final var identity = new IdentityEntity();

    subject.instantiateGithubProperties(githubResponse, identity);

    List<IdentityPropertyEntity> properties = identity.getIdentityProperties();
    assertThat(properties.size(), is(equalTo(2)));
    for (IdentityPropertyEntity property : properties) {
      if (property.getName().equals(IdentityPropertyEntityInstantiator.GITHUB_AVATAR_URL_KEY)) {
        assertThat(property.getValue(), is(equalTo(avatarUrl)));

      } else if (property.getName().equals(IdentityPropertyEntityInstantiator.GITHUB_LOGIN_KEY)) {
        assertThat(property.getValue(), is(equalTo(login)));
      } else {
        throw new RuntimeException("Property doesn't match expected: " + property.getName());
      }
    }
  }
}
