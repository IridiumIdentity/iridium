/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

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
