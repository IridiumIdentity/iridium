package software.iridium.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import software.iridium.api.validator.UserInfo;

import java.io.IOException;

@Component
public class PreAuthMdcFilter extends OncePerRequestFilter {
  public static final String MDC_CONTEXT_HOST_NAME = "HostName";
  public static final String MDC_CONTEXT_REQUEST_ID = "RequestId";
  public static final String MDC_CONTEXT_USER_ID = "UserId";

  @Value("${HOSTNAME:localhost}")
  private String hostname;

  private static int requestId = 0;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String path = getRequestPath(request);
    if (!path.equals("status")) {
      try {

        MDC.put(MDC_CONTEXT_HOST_NAME, hostname);
        MDC.put(MDC_CONTEXT_REQUEST_ID, String.valueOf(++requestId));
        MDC.put(MDC_CONTEXT_USER_ID, retrieveUserId());
        filterChain.doFilter(request, response);
      } finally {
        MDC.remove(MDC_CONTEXT_HOST_NAME);
        MDC.remove(MDC_CONTEXT_REQUEST_ID);
        MDC.remove(MDC_CONTEXT_USER_ID);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  protected String retrieveUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      Object principal = authentication.getPrincipal();
      if (principal != null && principal instanceof UserInfo) {
        return ((UserInfo) principal).getUsername();
      }
    }
    return "anonymous";
  }

  private String getRequestPath(HttpServletRequest request) {
    String url = request.getRequestURL().toString();
    int pathSegmentMarker = StringUtils.ordinalIndexOf(url, "/", 3);
    return url.substring(pathSegmentMarker + 1);
  }
}
