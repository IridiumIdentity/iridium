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
package software.iridium.tracker.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PerRequestCorsFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(PerRequestCorsFilter.class);

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain)
      throws ServletException, IOException {
    // todo (josh fischer) make this configurable
    response.addHeader("Access-Control-Allow-Origin", "*");

    logger.info("Sending Header....");
    response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, HEAD, DELETE, OPTIONS");
    response.addHeader(
        "Access-Control-Allow-Headers",
        "Authorization, Content-Type, Accept, X-IRIDIUM-AUTH-TOKEN, Access-Control-Allow-Origin,"
            + " Access-Control-Allow-Methods, Set-Cookie");

    response.addHeader("Access-Control-Max-Age", "3600");

    filterChain.doFilter(request, response);
  }
}
