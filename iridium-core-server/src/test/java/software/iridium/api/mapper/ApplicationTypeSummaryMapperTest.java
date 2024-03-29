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

import java.util.ArrayList;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.ApplicationTypeEntity;

class ApplicationTypeSummaryMapperTest {

  private ApplicationTypeSummaryMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ApplicationTypeSummaryMapper();
  }

  @Test
  public void mapToList_AllGood_MapsAsExpected() {
    final var id = "the id";
    final var name = "the name";
    final var entity = new ApplicationTypeEntity();
    entity.setId(id);
    entity.setName(name);
    final var list = new ArrayList<ApplicationTypeEntity>();
    list.add(entity);

    final var responses = subject.mapToList(list);

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);
    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getName(), is(equalTo(name)));
  }

  @Test
  public void mapToList_EmptyList_MapsAsExpected() {
    final var list = new ArrayList<ApplicationTypeEntity>();

    final var response = subject.mapToList(list);

    assertThat(response.isEmpty(), is(equalTo(true)));
  }
}
