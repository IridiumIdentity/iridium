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
package software.iridium.api.generator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.ExternalIdentityProviderEntity;

class ProviderUrlGeneratorTest {

  private ProviderUrlGenerator subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new ProviderUrlGenerator();
  }

  @Test
  public void generate_AllGood_GeneratesAsExpected() {
    final var baseUrl = "http://somewhere.com";
    final var clientId = "theclientId";
    final var clientSecret = "theClientSecret";
    final var code = "code";
    final var redirectUrl = "http://localhost/redirect";
    final var provider = new ExternalIdentityProviderEntity();
    provider.setAccessTokenRequestBaseUrl(baseUrl);
    provider.setClientId(clientId);
    provider.setClientSecret(clientSecret);
    provider.setRedirectUri(redirectUrl);

    final var response = subject.generate(provider, code);

    assertThat(
        response,
        is(
            equalTo(
                String.format(
                    "%s?client_id=%s&client_secret=%s&code=%s&redirect_url=%s",
                    baseUrl, clientId, clientSecret, code, redirectUrl))));
  }
}
