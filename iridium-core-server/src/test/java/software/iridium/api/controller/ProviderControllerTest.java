/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.controller;

import software.iridium.api.service.ProviderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProviderControllerTest {

    @Mock
    private ProviderService mockProviderService;
    @InjectMocks
    private ProviderController subject;

    @AfterEach
    public void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(mockProviderService);
    }

    @Test
    public void retrieveAllSummaries_AllGood_BehavesAsExpected() {

        subject.retrieveAllSummaries();

        verify(mockProviderService).retrieveAllSummaries();
    }
}
