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
package software.iridium.api.authentication.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import software.iridium.api.authentication.domain.AuthorizationResponse;
import software.iridium.api.base.client.ErrorHandler;
import software.iridium.api.base.error.ClientCallException;

public class ProviderAccessTokenRequestor {

  private final RestTemplate restTemplate;

  public ProviderAccessTokenRequestor(final RestTemplate restTemplate) {
    super();
    this.restTemplate = restTemplate;
  }

  public AuthorizationResponse requestAccessToken(final String url) {
    final var errorHandler = new ErrorHandler();
    final URI uri;

    try {
      uri = new URIBuilder(url).build();
    } catch (final URISyntaxException e) {
      throw new IllegalStateException("passed URL is invalid, url: " + url);
    }

    try {
      final var headers = new HttpHeaders();
      headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
      final var responseEntity =
          restTemplate.exchange(
              uri,
              HttpMethod.POST,
              new HttpEntity<>(headers),
              ParameterizedTypeReference.forType(AuthorizationResponse.class));

      if (!responseEntity.getStatusCode().is2xxSuccessful()) {
        errorHandler.handleErrors(responseEntity.getStatusCode());
      }
      return (AuthorizationResponse) responseEntity.getBody();

    } catch (final RestClientException rce) {
      throw new ClientCallException("Failure posting to provider", rce);
    }
  }
}
