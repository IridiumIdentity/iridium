/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.client;

import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.base.client.ErrorHandler;
import software.iridium.api.base.error.ClientCallException;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class ProviderProfileRequestor {

    private final RestTemplate restTemplate;

    public ProviderProfileRequestor(final RestTemplate restTemplate) {
        super();
        this.restTemplate = restTemplate;
    }

    public GithubProfileResponse requestGithubProfile(final String url, final String accessToken) {
        final var errorHandler = new ErrorHandler();
        final URI uri;

        try {
            uri = new URIBuilder(url).build();
        } catch (final URISyntaxException e) {
            throw new IllegalStateException("passed URL is invalid, url: " + url);
        }

        try {
            final var headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            final var responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    ParameterizedTypeReference.forType(GithubProfileResponse.class)
            );

            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                errorHandler.handleErrors(responseEntity.getStatusCode());
            }
            return (GithubProfileResponse) responseEntity.getBody();

        } catch (final RestClientException rce) {
            throw new ClientCallException("Failure posting to provider", rce);
        }

    }
}
