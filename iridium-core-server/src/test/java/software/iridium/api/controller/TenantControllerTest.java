/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.controller;

import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.service.TenantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantControllerTest {

    @Mock
    private TenantService mockTenantService;
    @Mock
    private HttpServletRequest mockServletRequest;
    @InjectMocks
    private TenantController subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(
                mockTenantService,
                mockServletRequest
        );
    }

    @Test
    public void getTenantSummaries_AllGood_BehavesAsExpected() {
        final var summaries = new ArrayList<TenantSummary>();

        when(mockTenantService.getTenantSummaries(same(mockServletRequest))).thenReturn(summaries);

        assertThat(subject.getTenantSummaries(mockServletRequest).getData(), sameInstance(summaries));

        verify(mockTenantService).getTenantSummaries(same(mockServletRequest));
    }

    @Test
    public void create_AllGood_BehavesAsExpected() {
        final var request = new CreateTenantRequest();

        subject.create(mockServletRequest, request);

        verify(mockTenantService).create(same(mockServletRequest), same(request));
    }
}
