/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.util;

public enum AuthorizationCodeFlowConstants {

    RESPONSE_TYPE("response_type"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    REDIRECT_URI("redirect_uri"),
    SCOPE("scope"),
    STATE("state"),
    CODE_CHALLENGE("code_challenge"),
    CODE_CHALLENGE_METHOD("code_challenge_method"),
    AUTHORIZATION_CODE("code"),
    AUTHORIZATION_CODE_GRANT_TYPE("authorization_code"),
    GRANT_TYPE("grant_type"),
    CODE_VERIFIER("code_verifier");

    private String value;

    public String getValue() {
        return value;
    }

    AuthorizationCodeFlowConstants(final String value) {
        this.value = value;
    }
}
