package software.iridium.api;

import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Test {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        final var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8381/identities"))
                .headers("Accept", "", HttpHeaders.AUTHORIZATION, "Bearer someToken")
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }
}
