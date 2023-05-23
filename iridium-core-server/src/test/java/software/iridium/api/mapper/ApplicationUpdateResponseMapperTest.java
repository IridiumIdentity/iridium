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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ApplicationTypeEntity;

class ApplicationUpdateResponseMapperTest {

  private ApplicationUpdateResponseMapper subject;

  @BeforeEach
  public void setUpForEachTest() {
    subject = new ApplicationUpdateResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var applicationId = "the app id";
    final var name = "the app name";
    final var orgId = "the org id";
    final var appTypeId = "the app type id";
    final var description = "the description";
    final var callbackUrl = "http://somewhere.com/callback";
    final var homepageUrl = "http://somewhere.com";
    final var privacyPolicyUrl = "http://privacy.com";

    final var applicationType = new ApplicationTypeEntity();
    applicationType.setId(appTypeId);
    final var entity = new ApplicationEntity();
    entity.setName(name);
    entity.setTenantId(orgId);
    entity.setApplicationType(applicationType);
    entity.setId(applicationId);
    entity.setPrivacyPolicyUrl(privacyPolicyUrl);
    entity.setDescription(description);
    entity.setRedirectUri(callbackUrl);
    entity.setHomePageUrl(homepageUrl);

    final var response = subject.map(entity);

    assertThat(response.getName(), is(equalTo(name)));
    assertThat(response.getApplicationTypeId(), is(equalTo(appTypeId)));
    assertThat(response.getTenantId(), is(equalTo(orgId)));
    assertThat(response.getId(), is(equalTo(applicationId)));
    assertThat(response.getDescription(), is(equalTo(description)));
    assertThat(response.getCallbackURL(), is(equalTo(callbackUrl)));
    assertThat(response.getHomepageURL(), is(equalTo(homepageUrl)));
    assertThat(response.getPrivacyPolicyUrl(), is(equalTo(privacyPolicyUrl)));
  }
}
