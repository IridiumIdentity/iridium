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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;
import software.iridium.api.base.domain.ApiResponse;
import software.iridium.api.base.error.NotAuthorizedException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (NotAuthorizedException e) {
      final Object errorResponse = getErrorResponse(request, e);
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write(convertObjectToJson(errorResponse));
    }
  }

  private String convertObjectToJson(Object object) throws JsonProcessingException {
    if (object == null) {
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
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
}
