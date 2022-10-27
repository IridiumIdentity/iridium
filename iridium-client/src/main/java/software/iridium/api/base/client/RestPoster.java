package software.iridium.api.base.client;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import software.iridium.api.base.error.ClientCallException;

import java.net.URI;
import java.net.URISyntaxException;

public class RestPoster<B, R> {

	private RestTemplate restTemplate;

	public RestPoster( final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public R post(
			final String url,
			final MultiValueMap<String, String> headers,
			final B body, final ParameterizedTypeReference<R> parameterizedTypeReference, final ErrorHandler errorHandler
	) {
		final URI uri;
		
		try {
			uri = new URIBuilder(url).build();
		} catch (final URISyntaxException e) {
			throw new IllegalStateException("passed URL is invalid, url: " + url);
		}
		
		try {
			final var responseEntity = restTemplate.exchange(
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
