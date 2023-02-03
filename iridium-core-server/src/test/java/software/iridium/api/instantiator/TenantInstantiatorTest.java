/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.Environment;

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
