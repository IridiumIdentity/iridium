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

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import software.iridium.api.authentication.domain.GoogleProfileResponse;
import software.iridium.entity.ExternalIdentityProviderEntity;

@Component
public class GoogleProfileFetcher {

  public GoogleProfileResponse fetch(
      final ExternalIdentityProviderEntity provider, final String accessToken) {

    HttpClient httpClient =
        HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(
                conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

    WebClient client =
        WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();

    return client
        .method(HttpMethod.GET)
        .uri(provider.getProfileRequestBaseUrl())
        .header("Authorization", "Bearer " + accessToken)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(GoogleProfileResponse.class)
        .block();
  }
}
