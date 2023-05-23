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
package software.iridium.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import software.iridium.entity.IdentityEmailEntity;
import software.iridium.entity.IdentityEntity;

@ExtendWith(MockitoExtension.class)
class AuthenticationGeneratorTest {

  @Mock private BCryptPasswordEncoder mockEncoder;
  @InjectMocks private AuthenticationGenerator subject;

  @Test
  public void generateAuthentication_AllGood_BehavesAsExpected() {
    final var identity = new IdentityEntity();
    final var encodedAuthToken = "encodedToken";
    final var emailAddress = "email@you.com";
    final var email = new IdentityEmailEntity();
    email.setEmailAddress(emailAddress);
    email.setPrimary(true);
    identity.setEmails(Arrays.asList(email));
    ReflectionTestUtils.setField(subject, "tokenTimeToLiveInMinutes", 4);

    when(mockEncoder.encode(anyString())).thenReturn(encodedAuthToken);

    final var auth = subject.generateAuthentication(identity);

    verify(mockEncoder, times(2)).encode(anyString());

    MatcherAssert.assertThat(auth.getAuthToken(), is(equalTo(encodedAuthToken)));
    MatcherAssert.assertThat(auth.getRefreshToken(), is(equalTo(encodedAuthToken)));
  }
}
