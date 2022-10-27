/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.generator;

import software.iridium.api.entity.ExternalIdentityProviderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ProviderUrlGeneratorTest {

    private ProviderUrlGenerator subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new ProviderUrlGenerator();
    }

    @Test
    public void generate_AllGood_GeneratesAsExpected() {
        final var baseUrl = "http://somewhere.com";
        final var clientId = "theclientId";
        final var clientSecret = "theClientSecret";
        final var code = "code";
        final var redirectUrl = "http://localhost/redirect";
        final var provider = new ExternalIdentityProviderEntity();
        provider.setAccessTokenRequestBaseUrl(baseUrl);
        provider.setClientId(clientId);
        provider.setClientSecret(clientSecret);
        provider.setRedirectUri(redirectUrl);

        final var response = subject.generate(provider, code);

        assertThat(response, is(equalTo(String.format("%s?client_id=%s&client_secret=%s&code=%s&redirect_url=%s", baseUrl, clientId, clientSecret, code, redirectUrl))));
    }
}
