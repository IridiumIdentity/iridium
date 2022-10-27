/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.instantiator;

import org.hamcrest.MatcherAssert;
import software.iridium.api.entity.ApplicationEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientSecretEntityInstantiatorTest {

    @Mock
    private BCryptPasswordEncoder mockEncoder;
    @InjectMocks
    private ClientSecretEntityInstantiator subject;

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
