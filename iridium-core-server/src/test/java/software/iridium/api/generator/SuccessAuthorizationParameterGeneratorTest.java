/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.generator;

import software.iridium.api.util.AuthorizationCodeFlowConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class SuccessAuthorizationParameterGeneratorTest {

    private SuccessAuthorizationParameterGenerator subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new SuccessAuthorizationParameterGenerator();
    }

    @Test
    public void generate_AllGood_GeneratesAsExpected() {
        final var authCode = "theAuthCode";
        final var state = "TheState";
        final var params = new HashMap<String, String>();
        params.put(AuthorizationCodeFlowConstants.STATE.getValue(), state);

        final var response = subject.generate(params, authCode);

        assertThat(response.size(), is(equalTo(2)));
    }

}
