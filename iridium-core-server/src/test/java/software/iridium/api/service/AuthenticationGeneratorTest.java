/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import org.hamcrest.MatcherAssert;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationGeneratorTest {

    @Mock
    private BCryptPasswordEncoder mockEncoder;
    @InjectMocks
    private AuthenticationGenerator subject;

    @Test
    public void generateAuthentication_AllGood_BehavesAsExpected() {
        final var identity = new IdentityEntity();
        final var encodedAuthToken = "encodedToken";
        final var emailAddress = "email@you.com";
        final var email = new IdentityEmailEntity();
        email.setEmailAddress(emailAddress);
        email.setPrimary(true);
        identity.setEmails(Arrays.asList(email));
        ReflectionTestUtils.setField(subject,"tokenTimeToLiveInMinutes", 4);

        when(mockEncoder.encode(anyString())).thenReturn(encodedAuthToken);

        final var auth = subject.generateAuthentication(identity);

        verify(mockEncoder, times(2)).encode(anyString());

        MatcherAssert.assertThat(auth.getAuthToken(), is(equalTo(encodedAuthToken)));
        MatcherAssert.assertThat(auth.getRefreshToken(), is(equalTo(encodedAuthToken)));
    }
}
