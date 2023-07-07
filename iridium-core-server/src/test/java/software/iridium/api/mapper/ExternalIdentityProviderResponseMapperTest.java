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
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.ExternalIdentityProviderEntity;

class ExternalIdentityProviderResponseMapperTest {

  private ExternalIdentityProviderResponseMapper subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ExternalIdentityProviderResponseMapper();
  }

  @Test
  public void map_AllGood_BehavesAsExpected() {
    final var id = "the id";
    final var clientId = "the client id";
    final var clientSecret = "the client secret";
    final var name = "the name";
    final var entity = new ExternalIdentityProviderEntity();
    entity.setId(id);
    entity.setClientId(clientId);
    entity.setClientSecret(clientSecret);
    entity.setName(name);

    final var response = subject.map(entity);

    assertThat(response.getId(), is(equalTo(id)));
    assertThat(response.getClientId(), is(equalTo(clientId)));
    assertThat(response.getClientSecret(), is(equalTo(clientSecret)));
    assertThat(response.getName(), is(equalTo(name)));
  }
}
