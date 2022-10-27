/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import org.hamcrest.MatcherAssert;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class TenantInstantiatorTest {

    private TenantInstantiator subject;

    @BeforeEach
    public void setupForEachTestCase() {
        subject = new TenantInstantiator();
    }

    @Test
    public void instantiate_AllGood_BehavesAsExpected() {
        final var subdomain = "the subdomain";
        final var environment = Environment.DEVELOPMENT;
        final var request = new CreateTenantRequest();
        request.setSubdomain(subdomain);
        request.setEnvironment(environment);

        final var response = subject.instantiate(request);

        MatcherAssert.assertThat(response.getSubdomain(), is(equalTo(subdomain)));
        MatcherAssert.assertThat(response.getEnvironment(), is(equalTo(environment)));
    }
}
