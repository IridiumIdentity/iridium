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
package software.iridium.api;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import reactor.netty.http.client.HttpClient;
import software.iridium.api.authentication.client.ProviderAccessTokenRequestor;
import software.iridium.api.fetcher.GitHubProfileFetcher;

@ComponentScan(basePackages = {"software.iridium"})
@EnableJpaRepositories(basePackages = {"software.iridium"})
@EnableScheduling
@SpringBootApplication
@EntityScan("software.iridium.entity")
public class Iridium implements WebMvcConfigurer {

  private static final Logger logger = LoggerFactory.getLogger(Iridium.class);

  @Bean
  public ClassLoaderTemplateResolver thymeleafTemplateResolver() {
    ClassLoaderTemplateResolver configurer = new ClassLoaderTemplateResolver();
    configurer.setPrefix("templates/");
    configurer.setCacheable(false);
    configurer.setSuffix(".html");
    configurer.setTemplateMode(TemplateMode.HTML);
    configurer.setCharacterEncoding("UTF-8");
    configurer.setOrder(0);
    configurer.setCheckExistence(true);
    return configurer;
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .responseTimeout(Duration.ofMillis(5000))
        .doOnConnected(
            conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
  }

  @Bean
  public WebClient webClient(HttpClient httpClient) {

    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ProviderAccessTokenRequestor accessTokenRequestor() {
    return new ProviderAccessTokenRequestor(restTemplate());
  }

  @Bean
  public GitHubProfileFetcher providerProfileRequestor() {
    return new GitHubProfileFetcher(restTemplate());
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    logger.info("Iridium running in UTC timezone :" + new Date());
  }

  public static void main(String... args) {
    SpringApplication.run(Iridium.class);
  }
}
