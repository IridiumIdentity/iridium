/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.entity.IdentityEmailEntity;

class EmailSendRequestInstantiatorTest {

  private EmailSendRequestInstantiator subject;

  @BeforeEach
  public void setUpForEachTestCase() {
    subject = new EmailSendRequestInstantiator();
  }

  @Test
  public void instantiate_AllGood_BehavesAsExpected() {
    final var emailSubject = "the subject";
    final var template = "email-template";
    final var email = new IdentityEmailEntity();
    final var emailAddress = "you@nowhere.com";
    email.setEmailAddress(emailAddress);
    final var props = new HashMap<String, Object>();

    final var response = subject.instantiate(email, emailSubject, props, template);

    assertThat(response.getSubject(), is(equalTo(emailSubject)));
    assertThat(response.getTo(), is(equalTo(emailAddress)));
    assertThat(response.getProperties(), sameInstance(props));
  }
}
