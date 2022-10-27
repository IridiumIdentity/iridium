package software.iridium.api.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class AttributeValidator {

    public boolean isPositive(final Long candidate) {
        return candidate != null && candidate > 0L;
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

    public boolean ifPresentAndIsNotBlankAndNoLongerThan(final String candidate, final Integer maxLength) {
        return candidate == null || this.isNotBlankAndNoLongerThan(candidate, maxLength);
    }

    public boolean isBlank(final String candidate) {
        return StringUtils.isBlank(candidate);
    }

    public boolean isNotBlank(final String candidate) {
        return StringUtils.isNotBlank(candidate);
    }

    public boolean isNotNull(final Boolean candidate) {
        return candidate != null;
    }

    public boolean isNotNull(final Object candidate) {
        return candidate != null;
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
