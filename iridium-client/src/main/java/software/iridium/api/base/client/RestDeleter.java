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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import software.iridium.api.base.error.ClientCallException;

public class RestDeleter<R> {

  private ErrorHandler errorHandler;
  private RestTemplate restTemplate;

  public RestDeleter(final ErrorHandler errorHandler, final RestTemplate restTemplate) {
    this.errorHandler = errorHandler;
    this.restTemplate = restTemplate;
  }

  public R delete(
      final String url,
      final HttpHeaders headers,
      ParameterizedTypeReference<R> parameterizedTypeReference) {
    final URI uri;

    try {
      uri = new URIBuilder(url).build();
    } catch (final URISyntaxException e) {
      throw new IllegalStateException("invalid URL passed");
    }

    try {
      final var responseEntity =
          restTemplate.exchange(
              uri, HttpMethod.DELETE, new HttpEntity<>(headers), parameterizedTypeReference);

      if (!responseEntity.getStatusCode().is2xxSuccessful()) {
        errorHandler.handleErrors(responseEntity.getStatusCode());
      }
      return (R) responseEntity.getBody();

    } catch (final RestClientException rce) {
      throw new ClientCallException("Unable to delete data", rce);
    }
  }
}
