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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;

@ExtendWith(MockitoExtension.class)
class EmailVerificationTokenEntityInstantiatorTest {

  @Mock private BCryptPasswordEncoder mockEncoder;
  @InjectMocks private EmailVerificationTokenEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    verifyNoMoreInteractions(mockEncoder);
  }

  @Test
  public void createEmailVerificationToken_AllGood_BehavesAsExpected() {
    final var identityId = "the-uuid";
    final var identity = new IdentityEntity();
    identity.setId(identityId);
    final var email = new IdentityEmailEntity();
    email.setIdentity(identity);
    final var encodedToken = "theEncodedToken";

    when(mockEncoder.encode(anyString())).thenReturn(encodedToken);

    final var response = subject.createEmailVerificationToken(email);

    verify(mockEncoder).encode(anyString());
    MatcherAssert.assertThat(response.getToken(), is(equalTo(encodedToken)));
    MatcherAssert.assertThat(response.getEmail(), sameInstance(email));
    MatcherAssert.assertThat(response.getExpiration(), notNullValue());
  }
}
