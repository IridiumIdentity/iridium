/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.client;

import software.iridium.api.authentication.domain.AuthorizationResponse;
import software.iridium.api.base.client.ErrorHandler;
import software.iridium.api.base.error.ClientCallException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

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
            final var responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    ParameterizedTypeReference.forType(AuthorizationResponse.class)
            );

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                errorHandler.handleErrors(responseEntity.getStatusCode());
            }
            return (AuthorizationResponse) responseEntity.getBody();

        } catch (final RestClientException rce) {
            throw new ClientCallException("Failure posting to provider", rce);
        }

    }

}
