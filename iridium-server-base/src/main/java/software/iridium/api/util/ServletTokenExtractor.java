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
import software.iridium.api.base.error.NotAuthorizedException;

@Component
public class ServletTokenExtractor {

  public static final String BEARER_PREFIX_WITH_SPACE = "Bearer ";
  public static final String BASIC_PREFIX_WITH_SPACE = "Basic ";
  public static final String IRIDIUM_TOKEN_HEADER_VALUE = "X-IRIDIUM-AUTH-TOKEN";

  public String extractBearerToken(final HttpServletRequest request) {
    final var header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || header.length() <= BEARER_PREFIX_WITH_SPACE.length()) {
      throw new NotAuthorizedException();
    }
    return header.substring(BEARER_PREFIX_WITH_SPACE.length());
  }

  public String extractBasicAuthToken(final HttpServletRequest request) {
    final var header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || header.isBlank()) {
      throw new NotAuthorizedException("basic auth token blank");
    }
    return header.substring(BASIC_PREFIX_WITH_SPACE.length());
  }

  public String extractIridiumToken(final HttpServletRequest request) {
    final var header = request.getHeader(IRIDIUM_TOKEN_HEADER_VALUE);

    if (header == null || header.isBlank()) {
      throw new NotAuthorizedException("iridium token blank");
    }

    return header.substring(BEARER_PREFIX_WITH_SPACE.length());
  }
}
