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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import software.iridium.api.util.DateUtils;
import software.iridium.entity.IdentityEntity;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenEntityInstantiatorTest {

  @Mock private BCryptPasswordEncoder mockEncoder;
  @InjectMocks private PasswordResetTokenEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEncoder);
  }

  @Test
  public void instantiate_AllGood_InstantiatesAsExpected() {
    ReflectionTestUtils.setField(subject, "passwordResetTokenLifetime", 4);
    final var resetToken = "the reset token";
    final var identity = new IdentityEntity();

    when(mockEncoder.encode(anyString())).thenReturn(resetToken);

    final var response = subject.instantiate(identity);

    verify(mockEncoder).encode(anyString());

    MatcherAssert.assertThat(response.getIdentity(), sameInstance(identity));
    MatcherAssert.assertThat(response.getToken(), is(equalTo(resetToken)));
    Assertions.assertTrue(response.getExpiration().before(DateUtils.addHoursToCurrentTime(5)));
    Assertions.assertTrue(response.getExpiration().after(DateUtils.addHoursToCurrentTime(3)));
  }
}
