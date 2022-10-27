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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ProfileEntity;

class ProfileResponseMapperTest {

  private ProfileResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ProfileResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var firstName = "the first name";
    final var lastName = "the last name";
    final var entity = new ProfileEntity();
    entity.setFirstName(firstName);
    entity.setLastName(lastName);

    final var response = subject.map(entity);

    MatcherAssert.assertThat(response.getFirstName(), is(equalTo(firstName)));
    MatcherAssert.assertThat(response.getLastName(), is(equalTo(lastName)));
  }

  @Test
  public void map_ProfileIsNull_ReturnsNull() {
    final var response = subject.map(null);

    assertThat(response, nullValue());
  }
}
