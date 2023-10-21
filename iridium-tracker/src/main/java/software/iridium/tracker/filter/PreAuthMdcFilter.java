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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import software.iridium.entity.user.UserInfo;

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
