/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ServletTokenExtractor {

    public final static String BEARER_PREFIX_WITH_SPACE = "Bearer ";
    public static final String BASIC_PREFIX_WITH_SPACE = "Basic ";
    public final static String IRIDIUM_TOKEN_HEADER_VALUE = "X-IRIDIUM-AUTH-TOKEN";

    public String extractBearerToken(final HttpServletRequest request) {
        // todo: make sure in the real environment we check bearer tokens ahead of time.
        return request.getHeader(HttpHeaders.AUTHORIZATION).substring(BEARER_PREFIX_WITH_SPACE.length());
    }

    public String extractBasicAuthToken(final HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION).substring(BASIC_PREFIX_WITH_SPACE.length());
    }

    public String extractIridiumToken(final HttpServletRequest request) {

        return request.getHeader(IRIDIUM_TOKEN_HEADER_VALUE).substring(BEARER_PREFIX_WITH_SPACE.length());
    }

}
