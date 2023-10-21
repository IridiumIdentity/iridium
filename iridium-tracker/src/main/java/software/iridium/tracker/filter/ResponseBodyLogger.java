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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseBodyLogger implements ResponseBodyAdvice<Object> {

  private static final Logger logger = LoggerFactory.getLogger(ResponseBodyLogger.class);

  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    displayResp(
        ((ServletServerHttpRequest) request).getServletRequest(),
        ((ServletServerHttpResponse) response).getServletResponse(),
        body);
    return body;
  }

  public void displayResp(HttpServletRequest request, HttpServletResponse response, Object body) {
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, String> headers = getHeaders(response);
    stringBuilder.append("RESPONSE ");
    stringBuilder.append(" method = [").append(request.getMethod()).append("]");
    if (!headers.isEmpty()) {
      stringBuilder.append(" ResponseHeaders = [").append(headers).append("]");
    }
    stringBuilder.append(" responseBody = [").append(body).append("]");

    logger.info("{}", stringBuilder);
  }

  private Map<String, String> getHeaders(HttpServletResponse response) {
    Map<String, String> headers = new HashMap<>();
    Collection<String> headerMap = response.getHeaderNames();
    for (String str : headerMap) {
      headers.put(str, response.getHeader(str));
    }
    return headers;
  }
}
