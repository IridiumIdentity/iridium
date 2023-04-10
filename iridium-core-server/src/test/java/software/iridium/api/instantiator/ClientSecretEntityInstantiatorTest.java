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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.iridium.api.entity.ApplicationEntity;

@ExtendWith(MockitoExtension.class)
class ClientSecretEntityInstantiatorTest {

  @Mock private BCryptPasswordEncoder mockEncoder;
  @InjectMocks private ClientSecretEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEncoder);
  }

  @Test
  public void instantiate_AllGood_BehavesAsExpected() {
    final var application = new ApplicationEntity();
    final var secret = "the secret";

    when(mockEncoder.encode(same(secret))).thenReturn(secret);

    final var response = subject.instantiate(application, secret);

    MatcherAssert.assertThat(response.getSecretKey(), is(equalTo(secret)));
    MatcherAssert.assertThat(response.getApplication(), sameInstance(application));
    assertThat(application.getClientSecrets().size(), is(equalTo(1)));
    assertThat(application.getClientSecrets().get(0), sameInstance(response));

    verify(mockEncoder).encode(same(secret));
  }
}
