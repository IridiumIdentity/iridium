/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.EncoderUtils;
import software.iridium.api.util.SHA256Hasher;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationCodeEntityInstantiatorTest {

    @Mock
    private EncoderUtils mockEncoderUtils;
    @Mock
    private SHA256Hasher mockSha256Hasher;
    @InjectMocks
    private AuthorizationCodeEntityInstantiator subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(
                mockEncoderUtils,
                mockSha256Hasher
        );
    }

    @Test
    public void instantiate_AllGoodS56_InstantiatesAsExpected() {
        final var params = new HashMap<String, String>();
        final var clientId = "the client id";
        params.put(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), clientId);
        final var identityId = "the identity id";
        final var codeChallengeMethod = "S256";
        params.put(AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue(), codeChallengeMethod);
        final var codeChallenge = "the code challenge";
        params.put(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue(), codeChallenge);
        final var redirectUri = "http://localhost:4200";
        params.put(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), redirectUri);
        final var identity = new IdentityEntity();
        identity.setId(identityId);
        final var authorizationCode = "the auth code";

        when(mockEncoderUtils.generateCryptoSecureString(same(AuthorizationCodeEntityInstantiator.AUTHORIZATION_CODE_MAX_LENGTH))).thenReturn(authorizationCode);

        final var entity = subject.instantiate(identity, params);

        verify(mockEncoderUtils).generateCryptoSecureString(same(AuthorizationCodeEntityInstantiator.AUTHORIZATION_CODE_MAX_LENGTH));

        MatcherAssert.assertThat(entity.getClientId(), is(equalTo(clientId)));
        MatcherAssert.assertThat(entity.getIdentityId(), is(equalTo(identityId)));
        MatcherAssert.assertThat(entity.getCodeChallenge(), is(equalTo(codeChallenge)));
        MatcherAssert.assertThat(entity.getCodeChallengeMethod(), is(equalTo(CodeChallengeMethod.S256)));
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
