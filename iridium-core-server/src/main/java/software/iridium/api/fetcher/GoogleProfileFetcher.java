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
package software.iridium.api.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import software.iridium.api.authentication.domain.GoogleProfileResponse;
import software.iridium.entity.ExternalIdentityProviderEntity;

@Component
public class GoogleProfileFetcher {

  @Autowired private WebClient webClient;

  public GoogleProfileResponse fetch(
      final ExternalIdentityProviderEntity provider, final String accessToken) {

    return webClient
        .method(HttpMethod.GET)
        .uri(provider.getProfileRequestBaseUrl())
        .header("Authorization", "Bearer " + accessToken)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(GoogleProfileResponse.class)
        .block();
  }
}
