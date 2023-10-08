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
package software.iridium.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;
import software.iridium.api.base.domain.ApiResponse;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.user.UserInfo;

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
      } catch (NotAuthorizedException e) {
        final Object errorResponse = getErrorResponse(request, e);
        setErrorResponse(response, errorResponse);
      } finally {
        MDC.remove(MDC_CONTEXT_HOST_NAME);
        MDC.remove(MDC_CONTEXT_REQUEST_ID);
        MDC.remove(MDC_CONTEXT_USER_ID);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private void setErrorResponse(HttpServletResponse response, Object errorResponse)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private Object getErrorResponse(HttpServletRequest request, NotAuthorizedException e) {
    logger.error("Not Authorized: ", e);
    final var acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
    final var shouldReturnJson = acceptHeader != null && acceptHeader.contains("json");

    if (shouldReturnJson) {
      return new ApiResponse("401", e.getMessage());
    }

    final var modelAndView = new ModelAndView();

    modelAndView.addObject("statusCode", e.getCode());
    modelAndView.addObject("errorMessage", e.getMessage());
    modelAndView.setViewName("error");
    return modelAndView;
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
