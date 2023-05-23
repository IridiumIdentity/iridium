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
package software.iridium.api.updator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.authentication.domain.ApplicationUpdateRequest;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ApplicationTypeEntity;

class ApplicationEntityUpdatorTest {

  private ApplicationEntityUpdator subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ApplicationEntityUpdator();
  }

  @Test
  public void update_AllGood_UpdatesAsExpected() {
    final var entity = new ApplicationEntity();

    final var homepageURL = "http://home.com";
    final var callbackURL = "http://home.com/callback";
    final var privacyPolicyURL = "http://home.com/privacy";
    final var iconURL = "http://home.com/icon-url";
    final var name = "the name";
    final var description = "the description";
    final var applicationType = new ApplicationTypeEntity();

    final var request = new ApplicationUpdateRequest();
    request.setDescription(description);
    request.setName(name);
    request.setHomePageUrl(homepageURL);
    request.setIconUrl(iconURL);
    request.setPrivacyPolicyUrl(privacyPolicyURL);
    request.setRedirectUri(callbackURL);

    final var response = subject.update(entity, applicationType, request);

    assertThat(response.getApplicationType(), is(equalTo(applicationType)));
    assertThat(response.getDescription(), is(equalTo(description)));
    assertThat(response.getName(), is(equalTo(name)));
    assertThat(response.getHomePageUrl(), is(equalTo(homepageURL)));
    assertThat(response.getPrivacyPolicyUrl(), is(equalTo(privacyPolicyURL)));
    assertThat(response.getRedirectUri(), is(equalTo(callbackURL)));
  }
}
