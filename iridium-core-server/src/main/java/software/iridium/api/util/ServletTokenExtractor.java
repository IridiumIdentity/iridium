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
package software.iridium.api.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class ServletTokenExtractor {

  public static final String BEARER_PREFIX_WITH_SPACE = "Bearer ";
  public static final String BASIC_PREFIX_WITH_SPACE = "Basic ";
  public static final String IRIDIUM_TOKEN_HEADER_VALUE = "X-IRIDIUM-AUTH-TOKEN";

  public String extractBearerToken(final HttpServletRequest request) {
    return request
        .getHeader(HttpHeaders.AUTHORIZATION)
        .substring(BEARER_PREFIX_WITH_SPACE.length());
  }

  public String extractBasicAuthToken(final HttpServletRequest request) {
    return request.getHeader(HttpHeaders.AUTHORIZATION).substring(BASIC_PREFIX_WITH_SPACE.length());
  }

  public String extractIridiumToken(final HttpServletRequest request) {

    return request
        .getHeader(IRIDIUM_TOKEN_HEADER_VALUE)
        .substring(BEARER_PREFIX_WITH_SPACE.length());
  }
}
