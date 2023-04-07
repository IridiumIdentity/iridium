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

package software.iridium.api.base.client;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import software.iridium.api.base.error.ClientCallException;

public class RestPoster<B, R> {

  private RestTemplate restTemplate;

  public RestPoster(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public R post(
      final String url,
      final MultiValueMap<String, String> headers,
      final B body,
      final ParameterizedTypeReference<R> parameterizedTypeReference,
      final ErrorHandler errorHandler) {
    final URI uri;

    try {
      uri = new URIBuilder(url).build();
    } catch (final URISyntaxException e) {
      throw new IllegalStateException("passed URL is invalid, url: " + url);
    }

    try {
      final var responseEntity =
          restTemplate.exchange(
              uri,
              HttpMethod.POST,
              new HttpEntity<>(body, new HttpHeaders(headers)),
              parameterizedTypeReference);

      if (!responseEntity.getStatusCode().is2xxSuccessful()) {
        errorHandler.handleErrors(responseEntity.getStatusCode());
      }
      return (R) responseEntity.getBody();

    } catch (final RestClientException rce) {
      throw new ClientCallException("Failure posting to service", rce);
    }
  }
}
