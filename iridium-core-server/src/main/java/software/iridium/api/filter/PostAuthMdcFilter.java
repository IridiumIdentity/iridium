package software.iridium.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PostAuthMdcFilter extends PreAuthMdcFilter {

  private static final Logger logger = LoggerFactory.getLogger(PostAuthMdcFilter.class);

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String path = getRequestPath(request);
    if (!path.equals("status")) {
      MDC.put(MDC_CONTEXT_USER_ID, retrieveUserId());
      filterChain.doFilter(request, response);
    }
  }

  private String getRequestPath(HttpServletRequest request) {
    String url = request.getRequestURL().toString();
    int pathSegmentMarker = StringUtils.ordinalIndexOf(url, "/", 3);
    return url.substring(pathSegmentMarker + 1);
  }
}
