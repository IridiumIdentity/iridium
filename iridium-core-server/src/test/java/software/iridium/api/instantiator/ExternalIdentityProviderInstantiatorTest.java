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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderRequest;
import software.iridium.entity.ExternalIdentityProviderParameterTemplateEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.entity.TenantEntity;

@ExtendWith(MockitoExtension.class)
class ExternalIdentityProviderInstantiatorTest {
  @Mock private ParameterHashMapInstantiator mockParamInstantiator;
  @InjectMocks private ExternalIdentityProviderInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockParamInstantiator);
  }

  @Test
  public void instantiate_AllGood_BehavesAsExpected() {
    final var name = "github";
    final var baseAuthorizationUrl = "https://base-auth.com";
    final var baseAccessTokenUrl = "https://base-access-token.com";
    final var baseProfileUrl = "https://base-profle.com";
    final var clientId = "some client id";
    final var clientSecret = "some client secret";
    final var defaultScope = "the default scope";
    final var iconPath = "the icon path";
    final var displayName = "the display name";
    final var tenant = new TenantEntity();
    final var template = new ExternalIdentityProviderTemplateEntity();
    template.setName(name);
    template.setIconPath(iconPath);
    template.setProfileRequestBaseUrl(baseProfileUrl);
    template.setBaseAuthorizationUrl(baseAuthorizationUrl);
    template.setAccessTokenRequestBaseUrl(baseAccessTokenUrl);
    template.setDefaultScope(defaultScope);
    template.setDisplayName(displayName);
    final var request = new CreateExternalIdentityProviderRequest();
    request.setClientSecret(clientSecret);
    request.setClientId(clientId);
    final var accessTokenParams = new ArrayList<ExternalIdentityProviderParameterTemplateEntity>();
    final var authorizationParams =
        new ArrayList<ExternalIdentityProviderParameterTemplateEntity>();
    template.setAccessTokenParameters(accessTokenParams);
    template.setAuthorizationParameters(authorizationParams);
    final var accessTokenParamMap = new HashMap<String, String>();
    final var authorizationParamMap = new HashMap<String, String>();

    when(mockParamInstantiator.instantiate(same(authorizationParams)))
        .thenReturn(authorizationParamMap);
    when(mockParamInstantiator.instantiate(same(accessTokenParams)))
        .thenReturn(accessTokenParamMap);

    final var response = subject.instantiate(tenant, request, template);

    verify(mockParamInstantiator).instantiate(same(authorizationParams));
    verify(mockParamInstantiator).instantiate(same(accessTokenParams));

    assertThat(response.getTemplate(), sameInstance(template));
    assertThat(response.getTenant(), sameInstance(tenant));
    assertThat(response.getName(), is(equalTo(name)));
    assertThat(response.getAccessTokenRequestBaseUrl(), is(equalTo(baseAccessTokenUrl)));
    assertThat(response.getBaseAuthorizationUrl(), is(equalTo(baseAuthorizationUrl)));
    assertThat(response.getClientId(), is(equalTo(clientId)));
    assertThat(response.getClientSecret(), is(equalTo(clientSecret)));
    assertThat(response.getAccessTokenParameters(), is(equalTo(accessTokenParamMap)));
    assertThat(response.getAuthorizationParameters(), is(equalTo(authorizationParamMap)));
    assertThat(response.getProfileRequestBaseUrl(), is(equalTo(baseProfileUrl)));
    assertThat(response.getScope(), is(equalTo(defaultScope)));
    assertThat(response.getIconPath(), is(equalTo(iconPath)));
    assertThat(response.getDisplayName(), is(equalTo(displayName)));
  }
}
