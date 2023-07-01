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

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.ExternalIdentityProviderEntity;

class ExternalProviderAccessTokenUrlGeneratorTest {

  private ExternalProviderAccessTokenUrlGenerator subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new ExternalProviderAccessTokenUrlGenerator();
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
                    "%s?code=%s&client_secret=%s&redirect_uri=%s&client_id=%s",
                    baseUrl, code, clientSecret, redirectUrl, clientId))));
  }

  @Test
  public void generate_CustomProviderParameters_GeneratesAsExpected() {
    final var baseUrl = "http://somewhere.com";
    final var clientId = "theclientId";
    final var clientSecret = "theClientSecret";
    final var code = "code";
    final var redirectUrl = "http://localhost/redirect";
    final var someParamValue = "some_param_value";
    final var provider = new ExternalIdentityProviderEntity();
    final var providerSpecificParams = new HashMap<String, String>();
    providerSpecificParams.put("some_param", someParamValue);
    provider.setAccessTokenParameters(providerSpecificParams);
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
                    "%s?some_param=%s&code=%s&client_secret=%s&redirect_uri=%s&client_id=%s",
                    baseUrl, someParamValue, code, clientSecret, redirectUrl, clientId))));
  }
}
