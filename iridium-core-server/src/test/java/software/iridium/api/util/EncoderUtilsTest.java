/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class EncoderUtilsTest {

    private EncoderUtils subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new EncoderUtils();
    }

    @Test
    public void cryptoSecureToHex_AllGood_generatesAsExpected() throws NoSuchAlgorithmException {
        final var length = 34;

        final var response = subject.cryptoSecureToHex(length);

        assertThat(response.length(), is(equalTo(length * 2)));
    }

    @Test
    public void generateCryptoSecureString_AllGood_GeneratesAsExpected() {
        final var length = 34;

        final var response = subject.generateCryptoSecureString(length);

        assertThat(response.length(), is(equalTo(length)));
    }
}
