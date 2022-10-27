/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.controller;

import software.iridium.api.authentication.domain.InitiatePasswordResetRequest;
import software.iridium.api.authentication.domain.PasswordResetRequest;
import software.iridium.api.service.PasswordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class PasswordControllerTest {

    @Mock
    private PasswordService mockPasswordService;
    @InjectMocks
    private PasswordController subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        verifyNoMoreInteractions(mockPasswordService);
    }

    @Test
    public void initiateForgotPassword_AllGood_BehavesAsExpected() {
        final var request = new InitiatePasswordResetRequest();

        subject.initiateResetPassword(request);

        verify(mockPasswordService).initiatePasswordReset(same(request));
    }

    @Test
    public void resetPassword_AllGood_BehavesAsExpected() {
        final var request = new PasswordResetRequest();

        subject.resetPassword(request);

        verify(mockPasswordService).resetPassword(same(request));
    }
}
