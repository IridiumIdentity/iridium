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

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ExternalIdentityProviderEntity;

@Component
public class RedirectUrlGenerator {

  public String generate(
      final String redirectBaseUrl,
      final ApplicationEntity application,
      final ExternalIdentityProviderEntity provider,
      final String state) {
    final var requestParams = new LinkedMultiValueMap<String, String>();
    final var params = provider.getAuthorizationParameters();
    params.put("client_id", provider.getClientId());
    params.put("state", state);
    params.put("redirect_uri", application.getRedirectUri());
    params.put("scope", provider.getScope());
    for (Map.Entry<String, String> entry : params.entrySet()) {
      requestParams.put(entry.getKey(), List.of(entry.getValue()));
    }
    return UriComponentsBuilder.fromUriString(redirectBaseUrl)
        .queryParams(requestParams)
        .buildAndExpand()
        .toUriString();
  }

  public String generate(final String redirectBaseUrl, final MultiValueMap<String, String> params) {
    return UriComponentsBuilder.fromUriString(redirectBaseUrl)
        .queryParams(params)
        .buildAndExpand()
        .toUriString();
  }
}
