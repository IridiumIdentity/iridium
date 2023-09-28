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

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.ExternalIdentityProviderEntity;

class ExternalProviderLoginDescriptorResponseMapperTest {

  private ExternalProviderLoginDescriptorResponseMapper subject;

  @BeforeEach
  public void setUpForEachTest() {
    subject = new ExternalProviderLoginDescriptorResponseMapper();
  }

  @Test
  public void mapList_AllGood_MapsAsExpected() {
    final var entities = new ArrayList<ExternalIdentityProviderEntity>();
    final var entity = new ExternalIdentityProviderEntity();
    final var scope = "the scope";
    final var clientId = "the client id";
    final var iconPath = "some path";
    final var responseType = "code";
    final var redirectUri = "redirectURI";
    final var name = "the name";
    final var displayName = "the display name";
    entity.setScope(scope);
    entity.setClientId(clientId);
    entity.setIconPath(iconPath);
    entity.setName(name);
    entity.setDisplayName(displayName);
    entities.add(entity);

    final var responses = subject.mapList(entities);

    assertThat(responses.size(), is(equalTo(1)));
    final var response = responses.get(0);

    assertThat(response.getScope(), is(equalTo(scope)));
    assertThat(response.getClientId(), is(equalTo(clientId)));
    assertThat(response.getIconPath(), is(equalTo(iconPath)));
    assertThat(response.getResponseType(), is(equalTo("code")));
    assertThat(response.getName(), is(equalTo(name)));
    assertThat(response.getDisplayName(), is(equalTo(displayName)));
  }
}
