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

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ApplicationTypeEntity;

class ApplicationCreateResponseMapperTest {

  private ApplicationCreateResponseMapper subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new ApplicationCreateResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var applicationId = "the app id";
    final var name = "the app name";
    final var orgId = "the org id";
    final var appTypeId = "the app type id";
    final var applicationType = new ApplicationTypeEntity();
    applicationType.setId(appTypeId);
    final var entity = new ApplicationEntity();
    entity.setName(name);
    entity.setTenantId(orgId);
    entity.setApplicationType(applicationType);
    entity.setId(applicationId);

    final var response = subject.map(entity);

    MatcherAssert.assertThat(response.getName(), is(equalTo(name)));
    MatcherAssert.assertThat(response.getApplicationTypeId(), is(equalTo(appTypeId)));
    MatcherAssert.assertThat(response.getTenantId(), is(equalTo(orgId)));
    MatcherAssert.assertThat(response.getId(), is(equalTo(applicationId)));
  }
}
