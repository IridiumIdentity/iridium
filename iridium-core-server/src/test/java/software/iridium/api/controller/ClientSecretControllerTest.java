/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.controller;

import software.iridium.api.service.ClientSecretService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClientSecretControllerTest {

    @Mock
    private ClientSecretService mockClientSecretService;
    @InjectMocks
    private ClientSecretController subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockClientSecretService);
    }

    @Test
    public void create_AllGood_BehavesAsExpected() {
        final var applicationId = "app id";

        subject.create(applicationId);

        verify(mockClientSecretService).create(same(applicationId));
    }

    @Test
    public void delete_AllGood_BehavesAsExpected() {
        final var applicationId = "the app id";
        final var clientSecretId = "the client secret";

        subject.delete(applicationId, clientSecretId);

        verify(mockClientSecretService).delete(same(applicationId), same(clientSecretId));
    }
}
