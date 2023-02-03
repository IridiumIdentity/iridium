/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.util;

import org.springframework.stereotype.Component;

@Component
public class SubdomainExtractor {

  private static final String SCHEME_SUBDOMAIN_SEPERATOR = "://";
  private static final String PERIOD = ".";

  public String extract(final String requestUrl) {
    final var schemeEndIndex = requestUrl.indexOf(SCHEME_SUBDOMAIN_SEPERATOR);
    final var firstPeriodIndex = requestUrl.indexOf(PERIOD);
    return requestUrl.substring(
        schemeEndIndex + SCHEME_SUBDOMAIN_SEPERATOR.length(), firstPeriodIndex);
  }
}
