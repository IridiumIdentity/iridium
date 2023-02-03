/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorsFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(CorsFilter.class);

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain)
      throws ServletException, IOException {
    response.addHeader("Access-Control-Allow-Origin", "http://localhost:4300");

    if (request.getHeader("Access-Control-Request-Method") != null
        && "OPTIONS".equals(request.getMethod())) {
      logger.trace("Sending Header....");
      // CORS "pre-flight" request
      response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
      response.addHeader(
          "Access-Control-Allow-Headers",
          "Authorization, Content-Type, Accept, X-IRIDIUM-AUTH-TOKEN");
      response.addHeader("Access-Control-Max-Age", "1");
    }

    filterChain.doFilter(request, response);
  }
}
