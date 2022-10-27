/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.domain;

public enum CodeChallengeMethod {

    PLAIN("plain"),
    S256("S256");

    private String value;

    public String getValue() {
        return value;
    }

    CodeChallengeMethod(final String value) {
        this.value = value;
    }
}
