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
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ClientSecretEntity;

class ClientSecretCreateResponseMapperTest {

  private ClientSecretCreateResponseMapper subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new ClientSecretCreateResponseMapper();
  }

  @Test
  public void map_AllGood_MapsAsExpected() {
    final var id = "the id";
    final var secretKey = "the secret key";
    final var created = new Date();
    final var entity = new ClientSecretEntity();
    entity.setId(id);
    entity.setCreated(created);
    entity.setSecretKey(secretKey);

    final var response = subject.map(entity, secretKey);

    MatcherAssert.assertThat(response.getId(), is(equalTo(id)));
    MatcherAssert.assertThat(response.getSecretKey(), is(equalTo(secretKey)));
    MatcherAssert.assertThat(response.getCreated(), is(equalTo(created)));
  }
}
