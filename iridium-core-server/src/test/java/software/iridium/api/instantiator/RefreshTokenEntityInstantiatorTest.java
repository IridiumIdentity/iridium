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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.util.TokenGenerator;

@ExtendWith(MockitoExtension.class)
class RefreshTokenEntityInstantiatorTest {

  @Mock private TokenGenerator mockTokenGenerator;
  @InjectMocks private RefreshTokenEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockTokenGenerator);
  }

  @Test
  public void instantiate_AllGood_BehavesAsExpected() {
    final var accessToken = "the access token";
    final var refreshToken = "the refresh token";

    when(mockTokenGenerator.generateRefreshToken(same(accessToken))).thenReturn(refreshToken);

    final var response = subject.instantiate(accessToken);

    assertThat(response.getRefreshToken(), is(equalTo(refreshToken)));
    verify(mockTokenGenerator).generateRefreshToken(same(accessToken));
  }
}
