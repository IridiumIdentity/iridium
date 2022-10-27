/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailVerificationTokenEntityInstantiatorTest {

    @Mock
    private BCryptPasswordEncoder mockEncoder;
    @InjectMocks
    private EmailVerificationTokenEntityInstantiator subject;

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
