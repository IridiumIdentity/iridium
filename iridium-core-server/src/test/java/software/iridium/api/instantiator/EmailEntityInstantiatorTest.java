/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class EmailEntityInstantiatorTest {

    private EmailEntityInstantiator subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new EmailEntityInstantiator();
    }

    @Test
    public void instantiatePrimaryEmail_AllGood_BehavesAsExpected() {
        final var emailAddress = "you@nowhere.com";

        final var response = subject.instantiatePrimaryEmail(emailAddress);

        MatcherAssert.assertThat(response.getEmailAddress(), is(equalTo(emailAddress)));
        MatcherAssert.assertThat(response.getPrimary(), is(equalTo(true)));
    }
}
