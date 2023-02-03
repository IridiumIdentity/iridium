/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
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

public class RestPutter<B, R> {

  private ErrorHandler errorHandler;
  private RestTemplate restTemplate;

  public RestPutter(final ErrorHandler errorHandler, final RestTemplate restTemplate) {
    this.errorHandler = errorHandler;
    this.restTemplate = restTemplate;
  }

  public R put(
      final String url, final MultiValueMap<String, String> headers, final B body, R response) {
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
              HttpMethod.PUT,
              new HttpEntity<>(body, new HttpHeaders(headers)),
              ParameterizedTypeReference.forType(response.getClass()));

      if (!responseEntity.getStatusCode().is2xxSuccessful()) {
        errorHandler.handleErrors(responseEntity.getStatusCode());
      }
      return (R) responseEntity.getBody();

    } catch (final RestClientException rce) {
      throw new ClientCallException("failure putting to service", rce);
    }
  }
}
