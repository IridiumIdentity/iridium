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

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.authentication.domain.Environment;
import software.iridium.api.entity.TenantEntity;

class CreateTenantResponseMapperTest {

  private CreateTenantResponseMapper subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new CreateTenantResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var id = "the id";
    final var subdomain = "the subdomin";
    final var environment = Environment.DEVELOPMENT;
    final var entity = new TenantEntity();
    entity.setId(id);
    entity.setSubdomain(subdomain);
    entity.setEnvironment(environment);

    final var response = subject.map(entity);

    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getEnvironment(), is(equalTo(environment)));
    MatcherAssert.assertThat(response.getSubdomain(), is(equalTo(subdomain)));
  }
}
