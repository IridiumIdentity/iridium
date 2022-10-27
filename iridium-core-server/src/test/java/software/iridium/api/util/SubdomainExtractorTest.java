/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class SubdomainExtractorTest {

    private SubdomainExtractor subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new SubdomainExtractor();
    }

    @Test
    public void extractHttp_AllGood_ReturnsAsExpected() {
        final var url = "http://some.url.com";

        final var result = subject.extract(url);

        assertThat(result, is(equalTo("some")));
    }

    @Test
    public void extractHttps_AllGood_ReturnsAsExpected() {
        final var url = "https://some-thing.url.com";

        final var result = subject.extract(url);

        assertThat(result, is(equalTo("some-thing")));
    }
}
