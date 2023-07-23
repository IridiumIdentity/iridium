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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AttributeValidator {

  public boolean isZeroOrGreater(final Long candidate) {
    return candidate != null && candidate >= 0L;
  }

  public boolean isZeroOrGreater(final Integer candidate) {
    return candidate != null && candidate >= 0;
  }

  public boolean isPositive(final Integer candidate) {
    return candidate != null && candidate > 0;
  }

  public boolean isValidSubdomain(final String candidate) {
    final var pattern = Pattern.compile("^[A-Za-z0-9-]*$");
    final var matcher = pattern.matcher(candidate);
    return matcher.find();
  }

  public boolean isNotBlankAndNoLongerThan(final String candidate, final Integer maxLength) {
    return StringUtils.isNotBlank(candidate) && candidate.length() <= maxLength;
  }

  public boolean ifPresentAndIsNotBlankAndNoLongerThan(
      final String candidate, final Integer maxLength) {
    return StringUtils.isEmpty(candidate) || this.isNotBlankAndNoLongerThan(candidate, maxLength);
  }

  public boolean isBlank(final String candidate) {
    return StringUtils.isBlank(candidate);
  }

  public boolean isNotBlank(final String candidate) {
    return StringUtils.isNotBlank(candidate) && !StringUtils.equals("null", candidate);
  }

  public boolean isNotNull(final Boolean candidate) {
    return candidate != null;
  }

  public boolean isNotNull(final Object candidate) {
    return candidate != null;
  }

  public boolean isValidUrl(String url) {
    if (url == null) {
      return false;
    }
    try {
      new URL(url);
      return true;
    } catch (MalformedURLException e) {
      return false;
    }
  }

  public boolean isUuid(String uuid) {
    if (StringUtils.isBlank(uuid)) {
      return false;
    } else {
      try {
        UUID.fromString(uuid);
        return true;
      } catch (IllegalArgumentException var3) {
        return false;
      }
    }
  }

  public boolean doesNotEqual(final String key, final String candidate) {
    return !StringUtils.equals(key, candidate);
  }

  public boolean equals(final String key, final String candidate) {
    return StringUtils.equals(key, candidate);
  }
}
