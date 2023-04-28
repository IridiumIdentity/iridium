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

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import software.iridium.api.authentication.client.AuthenticationApiClient;
import software.iridium.api.authentication.client.ProviderAccessTokenRequestor;
import software.iridium.api.authentication.client.ProviderProfileRequestor;

@PropertySource(
    ignoreResourceNotFound = false,
    value = {"classpath:application.properties"})
@ComponentScan(basePackages = {"software.iridium"})
@EnableJpaRepositories(basePackages = {"software.iridium.api.repository"})
@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AuthenticationApi implements WebMvcConfigurer {

  @Value("${software.iridium.emailApi.baseUrl}")
  private String emailApiBaseUrl;

  @Value("${software.iridium.identityApi.baseUrl}")
  private String identityApiBaseUrl;

  @Bean
  public ClassLoaderTemplateResolver thymeleafTemplateResolver() {
    ClassLoaderTemplateResolver configurer = new ClassLoaderTemplateResolver();
    configurer.setPrefix("templates/");
    configurer.setCacheable(false);
    configurer.setSuffix(".html");
    configurer.setTemplateMode(TemplateMode.HTML);
    configurer.setCharacterEncoding("UTF-8");
    configurer.setOrder(
        0); // this is important. This way spring //boot will listen to both places 0 and 1
    configurer.setCheckExistence(true);
    return configurer;
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
  public AuthenticationApiClient identityApiClient() {
    return new AuthenticationApiClient(identityApiBaseUrl, restTemplate());
  }

  @Bean
  public ProviderAccessTokenRequestor accessTokenRequestor() {
    return new ProviderAccessTokenRequestor(restTemplate());
  }

  @Bean
  public FreeMarkerConfigurer freemarkerClassLoaderConfig() {
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
    TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/email-templates");
    configuration.setTemplateLoader(templateLoader);
    FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
    freeMarkerConfigurer.setConfiguration(configuration);
    return freeMarkerConfigurer;
  }

  @Bean
  public ProviderProfileRequestor providerProfileRequestor() {
    return new ProviderProfileRequestor(restTemplate());
  }

  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    System.out.println("AuthenticationApi application running in UTC timezone :" + new Date());
  }

  public static void main(String... args) throws NoSuchAlgorithmException {
    SpringApplication.run(AuthenticationApi.class);
  }
}
