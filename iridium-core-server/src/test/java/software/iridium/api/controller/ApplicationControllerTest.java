/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.controller;

import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.authentication.domain.ApplicationCreateResponse;
import software.iridium.api.service.ApplicationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationControllerTest {

    @Mock
    private ApplicationService mockService;
    @InjectMocks
    private ApplicationController subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockService);
    }

    @Test
    public void create_AllGood_BehavesAsExpected() {
        final var request = new ApplicationCreateRequest();
        final var appResponse = new ApplicationCreateResponse();
        final var tenantId = "the id";

        when(mockService.create(same(request), same(tenantId))).thenReturn(appResponse);

        final var response = subject.create(request, tenantId);

        verify(mockService).create(same(request), same(tenantId));

        assertThat(response.getData(), sameInstance(appResponse));
    }

    @Test
    public void getPageByTenantIdAndApplicationTypeAllGood_BehavesAsExpected() {
        final var orgId = "the org id";
        final var applicationTypeId = "applicationTypeId";
        final var page = 1;
        final var size = 20;

        subject.getPageByTenantAndApplicationType(orgId, applicationTypeId, page, size,true);

        verify(mockService).getPageByTenantIdAndApplicationTypeId(same(orgId), same(applicationTypeId), same(page), same(size), eq(true));
    }
}
