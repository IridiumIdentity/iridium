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
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.iridium.api.entity.PasswordResetTokenEntity;

class SelfUrlGeneratorTest {

  private SelfUrlGenerator subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new SelfUrlGenerator();
  }

  @Test
  public void generatorChangePasswordUrl_AllGood_GeneratesAsExpected() {
    final var baseUrl = "http://localhost:8009/";
    ReflectionTestUtils.setField(subject, "baseUrl", baseUrl);
    final var token = "theToken";
    final var resetToken = new PasswordResetTokenEntity();
    resetToken.setToken(token);
    final var clientId = "the-client-id";

    final var response = subject.generateChangePasswordUrl(resetToken, clientId);

    assertThat(
        response,
        is(equalTo(baseUrl + "reset-password?token=" + token + "&client_id=" + clientId)));
  }
}
