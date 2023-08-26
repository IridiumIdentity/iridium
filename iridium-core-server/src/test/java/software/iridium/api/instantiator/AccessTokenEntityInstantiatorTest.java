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
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.util.TokenGenerator;
import software.iridium.entity.RefreshTokenEntity;

@ExtendWith(MockitoExtension.class)
class AccessTokenEntityInstantiatorTest {

  @Mock private TokenGenerator mockTokenGenerator;
  @Mock private RefreshTokenEntityInstantiator mockRefreshTokenEntityInstantiator;
  @Mock private RefreshTokenEntity mockRefreshTokenEntity;
  @InjectMocks private AccessTokenEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
            mockTokenGenerator, mockRefreshTokenEntityInstantiator, mockRefreshTokenEntity);
  }
  @Test
  public void instantiate_AllGood_BehavesAsExpected() {
    final var identityId = "the id";
    final var accessTokenValue = "the access token value";

    when(mockTokenGenerator.generateAccessToken(same(identityId), any(Date.class)))
        .thenReturn(accessTokenValue);
    when(mockRefreshTokenEntityInstantiator.instantiate(accessTokenValue))
            .thenReturn(mockRefreshTokenEntity);

    final var response = subject.instantiate(identityId);

    verify(mockTokenGenerator).generateAccessToken(same(identityId), any(Date.class));
    verify(mockRefreshTokenEntityInstantiator).instantiate(same(accessTokenValue));

    assertThat(response.getAccessToken(), is(equalTo(accessTokenValue)));
    assertThat(response.getTokenType(), is(equalTo("Bearer")));
    assertThat(response.getExpiration(), is(notNullValue()));
  }
}
