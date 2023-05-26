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
import software.iridium.entity.IdentityEmailEntity;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.ProfileEntity;

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
