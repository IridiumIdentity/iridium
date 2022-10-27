/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.handler;

import software.iridium.api.email.client.EmailApiClient;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.instantiator.EmailSendRequestInstantiator;
import software.iridium.api.instantiator.SelfUrlGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEventHandlerTest {

    @Mock
    private EmailSendRequestInstantiator mockEmailInstantiator;
    @Mock
    private SelfUrlGenerator mockUrlGenerator;
    @Mock
    private EmailApiClient mockEmailApiClient;
    @InjectMocks
    private PasswordEventHandler subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(
                mockEmailApiClient,
                mockUrlGenerator,
                mockEmailInstantiator
        );
    }

    @Test
    public void handlePasswordResetEvent_AllGood_BehavesAsExpected() {
        final var identity = new IdentityEntity();
        final var email = new IdentityEmailEntity();
        email.setPrimary(true);
        identity.getEmails().add(email);
        final var sendRequest = new EmailSendRequest();

        when(mockEmailInstantiator.instantiate(same(email), eq("Iridium Password Change Notification"), anyMap(), eq("confirm-password-reset"))).thenReturn(sendRequest);

        subject.handlePasswordResetEvent(identity);

        verify(mockEmailInstantiator).instantiate(same(email), eq("Iridium Password Change Notification"), anyMap(), eq("confirm-password-reset"));
        verify(mockEmailApiClient).sendNewIdentityVerificationMail(same(sendRequest));
    }

}
