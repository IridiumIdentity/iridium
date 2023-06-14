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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.IdentityEmailEntity;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.ProfileEntity;

class IdentitySummaryResponseMapperTest {

  private IdentitySummaryResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new IdentitySummaryResponseMapper();
  }

  @Test
  public void mapList_AllGood_BehavesAsExpected() {
    final var entityId = "the id";
    final var firstName = "the first name";
    final var lastName = "the last name";
    final var emailAddress = "you@somwhere.com";
    final var entity = new IdentityEntity();
    final var profile = new ProfileEntity();
    final var email = new IdentityEmailEntity();
    email.setPrimary(true);
    email.setEmailAddress(emailAddress);
    profile.setFirstName(firstName);
    profile.setLastName(lastName);
    entity.setId(entityId);
    entity.setProfile(profile);
    entity.getEmails().add(email);

    final var responses = subject.mapToSummaries(Arrays.asList(entity));

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);
    MatcherAssert.assertThat(response.getId(), is(equalTo(entityId)));
    MatcherAssert.assertThat(response.getFirstName(), is(equalTo(firstName)));
    MatcherAssert.assertThat(response.getLastName(), is(equalTo(lastName)));
    assertThat(response.getEmailAddress(), is(equalTo(emailAddress)));
  }

  @Test
  public void mapList_EmtpyList_ReturnsAsExpected() {

    final var response = subject.mapToSummaries(Arrays.asList());

    assertThat(response.size(), is(equalTo(0)));
  }
}
