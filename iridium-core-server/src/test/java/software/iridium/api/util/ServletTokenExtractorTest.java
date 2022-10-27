/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.util;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServletTokenExtractorTest {

    @Mock
    private HttpServletRequest mockServletRequest;
    @InjectMocks
    private ServletTokenExtractor subject;

    @AfterEach
    public void ensureNoMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockServletRequest);
    }

    @Test
    public void extract_AllGood_ExtractsAsExpected() {
        final var headerValue = "Bearer someTokenValue";

        when(mockServletRequest.getHeader(same(HttpHeaders.AUTHORIZATION))).thenReturn(headerValue);

        final var response = subject.extractBearerToken(mockServletRequest);

        verify(mockServletRequest).getHeader(same(HttpHeaders.AUTHORIZATION));

    }
}
