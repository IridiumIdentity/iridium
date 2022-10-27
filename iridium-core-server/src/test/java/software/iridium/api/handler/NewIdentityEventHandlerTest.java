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
class NewIdentityEventHandlerTest {

    @Mock
    private EmailApiClient mockEmailApiClient;
    @Mock
    private EmailSendRequestInstantiator mockEmailSendRequestInstantiator;
    @InjectMocks
    private NewIdentityEventHandler subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockEmailApiClient, mockEmailSendRequestInstantiator);
    }

    @Test
    public void handleEvent_AllGood_HandlesAsExpected() {
        final var tempPassword = "theTempPassword";
        final var primaryEmail = new IdentityEmailEntity();
        primaryEmail.setPrimary(true);
        final var identity = new IdentityEntity();
        identity.getEmails().add(primaryEmail);
        EmailSendRequest sendRequest = new EmailSendRequest();
        final var clientId = "theClientId";

        when(mockEmailSendRequestInstantiator.instantiate(same(primaryEmail), eq("Iridium Email Verification"), anyMap(), eq("new-identity"))).thenReturn(sendRequest);

        subject.handleEvent(identity, clientId);

        verify(mockEmailApiClient).sendNewIdentityVerificationMail(same(sendRequest));
        verify(mockEmailSendRequestInstantiator).instantiate(same(primaryEmail), eq("Iridium Email Verification"), anyMap(), eq("new-identity"));
    }
}
