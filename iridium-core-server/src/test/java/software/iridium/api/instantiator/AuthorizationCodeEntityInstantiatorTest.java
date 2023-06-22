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
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.model.AuthorizationRequestHolder;
import software.iridium.api.util.EncoderUtils;
import software.iridium.api.util.SHA256Hasher;
import software.iridium.entity.IdentityEntity;

@ExtendWith(MockitoExtension.class)
class AuthorizationCodeEntityInstantiatorTest {

  @Mock private EncoderUtils mockEncoderUtils;
  @Mock private SHA256Hasher mockSha256Hasher;
  @InjectMocks private AuthorizationCodeEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEncoderUtils, mockSha256Hasher);
  }

  @Test
  public void instantiate_AllGoodS56_InstantiatesAsExpected() {
    final var holder = new AuthorizationRequestHolder();

    final var clientId = "the client id";
    holder.setClientId(clientId);

    final var codeChallengeMethod = "S256";
    holder.setCodeChallengeMethod(codeChallengeMethod);

    final var codeChallenge = "the code challenge";
    holder.setCodeChallenge(codeChallenge);

    final var redirectUri = "http://localhost:4200";
    holder.setRedirectUri(redirectUri);

    final var identityId = "the identity id";
    final var identity = new IdentityEntity();
    identity.setId(identityId);

    final var authorizationCode = "the auth code";
    when(mockEncoderUtils.generateCryptoSecureString(
            same(AuthorizationCodeEntityInstantiator.AUTHORIZATION_CODE_MAX_LENGTH)))
        .thenReturn(authorizationCode);

    final var entity = subject.instantiate(identity, holder);

    verify(mockEncoderUtils)
        .generateCryptoSecureString(
            same(AuthorizationCodeEntityInstantiator.AUTHORIZATION_CODE_MAX_LENGTH));

    MatcherAssert.assertThat(entity.getClientId(), is(equalTo(clientId)));
    MatcherAssert.assertThat(entity.getIdentityId(), is(equalTo(identityId)));
    MatcherAssert.assertThat(entity.getCodeChallenge(), is(equalTo(codeChallenge)));
    MatcherAssert.assertThat(
        entity.getCodeChallengeMethod(), is(equalTo(CodeChallengeMethod.S256)));
    MatcherAssert.assertThat(entity.getRedirectUri(), is(equalTo(redirectUri)));
    MatcherAssert.assertThat(entity.getAuthorizationCode(), is(equalTo(authorizationCode)));
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, 2);
    final var endDate = cal.getTime();
    MatcherAssert.assertThat(entity.getExpiration(), is(greaterThan(new Date())));
    MatcherAssert.assertThat(entity.getExpiration(), is(lessThan(endDate)));
  }
}
