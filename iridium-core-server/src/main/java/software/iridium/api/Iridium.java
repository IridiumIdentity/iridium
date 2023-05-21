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
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import software.iridium.api.authentication.client.ProviderAccessTokenRequestor;
import software.iridium.api.authentication.client.ProviderProfileRequestor;

@ComponentScan(basePackages = {"software.iridium"})
@EnableJpaRepositories(basePackages = {"software.iridium.api.repository"})
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
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
    logger.info("Iridium running in UTC timezone :" + new Date());
  }

  public static void main(String... args) {
    SpringApplication.run(Iridium.class);
  }
}
