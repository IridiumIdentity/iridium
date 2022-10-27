/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.generator;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class RedirectUrlGeneratorTest {

    private RedirectUrlGenerator subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new RedirectUrlGenerator();
    }

    @Test
    public void generate_AllGood_GeneratesAsExpected() {
        final var baseUrl = "https://somewhere.com";
        final var paramMap = new LinkedMultiValueMap<String, String>();
        paramMap.put("param1", Arrays.asList("one"));
        paramMap.put("param2", Arrays.asList("two"));

        final var response = subject.generate(baseUrl, paramMap);

        assertThat(response, is(equalTo("https://somewhere.com?param1=one&param2=two")));
    }
}
