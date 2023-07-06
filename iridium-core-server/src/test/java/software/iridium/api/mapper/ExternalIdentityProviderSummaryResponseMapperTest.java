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
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.ExternalIdentityProviderEntity;

class ExternalIdentityProviderSummaryResponseMapperTest {

  private ExternalIdentityProviderSummaryResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ExternalIdentityProviderSummaryResponseMapper();
  }

  @Test
  public void mapList_AllGood_BehavesAsExpected() {
    final var entityId = "the id";
    final var entityName = "the name";
    final var iconPath = "theIconPath";
    final var entity = new ExternalIdentityProviderEntity();
    entity.setId(entityId);
    entity.setName(entityName);
    entity.setIconPath(iconPath);

    final var responses = subject.mapList(Arrays.asList(entity));

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);
    MatcherAssert.assertThat(response.getId(), is(equalTo(entityId)));
    MatcherAssert.assertThat(response.getName(), is(equalTo(entityName)));
    MatcherAssert.assertThat(response.getIconPath(), is(equalTo(iconPath)));
  }

  @Test
  public void mapList_EmtpyList_ReturnsAsExpected() {

    final var response = subject.mapList(Arrays.asList());

    assertThat(response.size(), is(equalTo(0)));
  }
}
