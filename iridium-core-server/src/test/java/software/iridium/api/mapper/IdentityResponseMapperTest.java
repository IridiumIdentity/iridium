/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.mapper;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.AuthenticationResponse;
import software.iridium.api.authentication.domain.ProfileResponse;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.ProfileEntity;

@ExtendWith(MockitoExtension.class)
class IdentityResponseMapperTest {

  @Mock private ProfileResponseMapper mockProfileMapper;
  @InjectMocks private IdentityResponseMapper subject;

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var id = "entityId";
    final var emailAddress = "email@uou.com";
    final var profileEntity = new ProfileEntity();
    final var entity = new IdentityEntity();
    final var email = new IdentityEmailEntity();
    email.setEmailAddress(emailAddress);
    email.setPrimary(true);
    entity.setId(id);
    entity.setEmails(Arrays.asList(email));
    entity.setProfile(profileEntity);
    final var profile = new ProfileResponse();
    final var appBaseUrl = "http://somewhere.com";
    final var tenantWebsite = "http://somewhereelse.com";
    final var applicationName = "the app name";
    final var userToken = "the token value";

    final var authenticationResponse =
        AuthenticationResponse.of(
            userToken, null, true, applicationName, tenantWebsite, appBaseUrl, null, null);

    when(mockProfileMapper.map(same(profileEntity))).thenReturn(profile);

    final var response = subject.map(entity, authenticationResponse);

    verify(mockProfileMapper).map(same(profileEntity));

    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getUsername(), is(equalTo(emailAddress)));
    MatcherAssert.assertThat(response.getProfile(), sameInstance(profile));
    MatcherAssert.assertThat(response.getApplicationName(), is(equalTo(applicationName)));
    MatcherAssert.assertThat(response.getTenantWebsite(), is(equalTo(tenantWebsite)));
    MatcherAssert.assertThat(response.getAppBaseUrl(), is(equalTo(appBaseUrl)));
    MatcherAssert.assertThat(response.getUserToken(), is(equalTo(userToken)));
  }
}
