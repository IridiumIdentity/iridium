/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.entity.IdentityEmailEntity;

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
