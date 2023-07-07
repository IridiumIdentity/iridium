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
package software.iridium.api.updator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.authentication.domain.ExternalIdentityProviderUpdateRequest;
import software.iridium.entity.ExternalIdentityProviderEntity;

class ExternalIdentityProviderUpdatorTest {

  private ExternalIdentityProviderUpdator subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new ExternalIdentityProviderUpdator();
  }

  @Test
  public void update_AllGood_UpdatesAsExpected() {
    final var clientId = "the client id";
    final var clientSecret = "the client secret";
    final var entity = new ExternalIdentityProviderEntity();
    final var request = new ExternalIdentityProviderUpdateRequest();
    request.setClientId(clientId);
    request.setClientSecret(clientSecret);

    final var response = subject.update(entity, request);

    assertThat(response.getClientId(), is(equalTo(clientId)));
    assertThat(response.getClientSecret(), is(equalTo(clientSecret)));
  }
}
