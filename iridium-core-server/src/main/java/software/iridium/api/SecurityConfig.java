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

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import software.iridium.api.filter.ExceptionHandlerFilter;
import software.iridium.api.filter.PostAuthMdcFilter;
import software.iridium.api.filter.PreAuthMdcFilter;
import software.iridium.api.filter.RequestLoggingFilter;
import software.iridium.api.repository.AccessTokenEntityRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Resource private RequestLoggingFilter requestLoggingFilter;
  @Resource private PreAuthMdcFilter preAuthMdcFilter;
  @Resource private ExceptionHandlerFilter exceptionHandlerFilter;
  @Resource private PostAuthMdcFilter postAuthMdcFilter;
  @Resource private AccessTokenEntityRepository accessTokenRepository;

  @Autowired private Environment env;

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/oauth/token");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    String[] profiles = env.getActiveProfiles();

    if (profiles != null && profiles.length > 0 && profiles[0].equals("h2")) {
      http.authorizeHttpRequests(
              (authorize) ->
                  authorize
                      .requestMatchers(toH2Console())
                      .permitAll()
                      .requestMatchers(
                          "/",
                          "/login",
                          "/styles/**",
                          "img/**",
                          "/oauth/external/authorize",
                          "/identities",
                          "/authorize",
                          "/authenticate",
                          "oauth/authorize",
                          "register",
                          "/oauth/token")
                      .permitAll()
                      .anyRequest()
                      .fullyAuthenticated())
          .apply(new AuthenticationManagerConfigurer());
      http.headers().frameOptions().disable();
      http.csrf().ignoringRequestMatchers(toH2Console());
    } else {
      http.authorizeHttpRequests(
              (authorize) ->
                  authorize
                      .requestMatchers(
                          "/",
                          "/login",
                          "/styles/**",
                          "img/**",
                          "/oauth/external/authorize",
                          "/identities",
                          "/authorize",
                          "/authenticate",
                          "oauth/authorize",
                          "register",
                          "/oauth/token")
                      .permitAll()
                      .anyRequest()
                      .fullyAuthenticated())
          .apply(new AuthenticationManagerConfigurer());
      http.csrf().disable();
    }

    http.addFilterBefore(exceptionHandlerFilter, TokenAuthenticationFilter.class);
    http.addFilterBefore(preAuthMdcFilter, TokenAuthenticationFilter.class);
    http.addFilterAfter(postAuthMdcFilter, TokenAuthenticationFilter.class);
    http.addFilterAfter(requestLoggingFilter, TokenAuthenticationFilter.class);
    http.cors();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    return http.build();
  }

  public class AuthenticationManagerConfigurer
      extends AbstractHttpConfigurer<AuthenticationManagerConfigurer, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) {
      AuthenticationManager authenticationManager =
          http.getSharedObject(AuthenticationManager.class);
      http.addFilter(new TokenAuthenticationFilter(authenticationManager, accessTokenRepository));
    }
  }

  @Bean
  public PreAuthenticatedAuthenticationProvider authenticationProvider() {
    final PreAuthenticatedAuthenticationProvider authenticationProvider =
        new PreAuthenticatedAuthenticationProvider();
    authenticationProvider.setPreAuthenticatedUserDetailsService(
        preAuthenticatedAuthenticationToken ->
            (UserDetails) preAuthenticatedAuthenticationToken.getPrincipal());
    return authenticationProvider;
  }
}
