/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.entity.PasswordResetTokenEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class SelfUrlGeneratorTest {

    private SelfUrlGenerator subject;

    @BeforeEach
    public void setUpForEachTestCase() {
        subject = new SelfUrlGenerator();
    }

    @Test
    public void generatorChangePasswordUrl_AllGood_GeneratesAsExpected() {
        final var baseUrl = "http://localhost:8009/";
        ReflectionTestUtils.setField(subject, "baseUrl", baseUrl);
        final var token = "theToken";
        final var resetToken = new PasswordResetTokenEntity();
        resetToken.setToken(token);
        final var clientId = "the-client-id";

        final var response = subject.generateChangePasswordUrl(resetToken, clientId);

        assertThat(response, is(equalTo(baseUrl + "reset-password?token=" + token + "&client_id=" + clientId)));
    }
}
