/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import software.iridium.api.authentication.domain.*;
import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.TenantEntity;
import software.iridium.api.instantiator.LoginDescriptorEntityInstantiator;
import software.iridium.api.instantiator.TenantInstantiator;
import software.iridium.api.mapper.CreateTenantResponseMapper;
import software.iridium.api.mapper.TenantSummaryMapper;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    private IdentityService mockIdentityService;
    @Mock
    private TenantEntityRepository mockTenantRepository;
    @Mock
    private TenantSummaryMapper mockSummaryMapper;
    @Mock
    private HttpServletRequest mockServletRequest;
    @Mock
    private AttributeValidator mockAttributeValidator;
    @Mock
    private IdentityEntityRepository mockIdentityRepository;
    @Mock
    private TenantInstantiator mockTenantInstantiator;
    @Mock
    private CreateTenantResponseMapper mockResponseMapper;
    @Mock
    private LoginDescriptorEntityInstantiator mockLoginDescriptorInstantiator;
    @InjectMocks
    private TenantService subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(
                mockIdentityService,
                mockTenantRepository,
                mockSummaryMapper,
                mockServletRequest,
                mockAttributeValidator,
                mockIdentityRepository,
                mockTenantInstantiator,
                mockResponseMapper,
                mockLoginDescriptorInstantiator
            );
    }

    @Test
    public void getTenantSummaries_AllGood_BehavesAsExpected() {
        final var identityResponse = new IdentityResponse();
        final var tenants = new ArrayList<TenantEntity>();
        final var tenantIds = new ArrayList<String>();
        identityResponse.setTenantIds(tenantIds);
        final var summaries = new ArrayList<TenantSummary>();

        when(mockIdentityService.getIdentity(same(mockServletRequest))).thenReturn(identityResponse);
        when(mockTenantRepository.findByIdIn(same(tenantIds))).thenReturn(Optional.of(tenants));
        when(mockSummaryMapper.mapToList(same(tenants))).thenReturn(summaries);

        assertThat(subject.getTenantSummaries(mockServletRequest), sameInstance(summaries));

        verify(mockIdentityService).getIdentity(same(mockServletRequest));
        verify(mockTenantRepository).findByIdIn(same(tenantIds));
        verify(mockSummaryMapper).mapToList(same(tenants));
    }

    @Test
    public void create_AllGood_BehavesAsExpected() {

        final var identityResponse = new IdentityResponse();
        final var identityId = "the id";
        identityResponse.setId(identityId);
        final var subdomain = "the-s9ubdomain";
        final var env = Environment.DEVELOPMENT;
        final var request = new CreateTenantRequest();
        request.setSubdomain(subdomain);
        request.setEnvironment(env);
        final var identityEntity = new IdentityEntity();
        final var entity = new TenantEntity();
        final var response = new CreateTenantResponse();

        when(mockAttributeValidator.isValidSubdomain(same(subdomain))).thenCallRealMethod();
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(same(subdomain), eq(100))).thenCallRealMethod();
        when(mockAttributeValidator.isNotNull(same(env))).thenCallRealMethod();
        when(mockTenantRepository.findBySubdomain(same(subdomain))).thenReturn(Optional.empty());
        when(mockIdentityService.getIdentity(same(mockServletRequest))).thenReturn(identityResponse);
        when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.of(identityEntity));
        when(mockTenantInstantiator.instantiate(same(request))).thenReturn(entity);
        when(mockTenantRepository.save(same(entity))).thenReturn(entity);
        when(mockResponseMapper.map(same(entity))).thenReturn(response);

        subject.create(mockServletRequest, request);

        verify(mockAttributeValidator).isValidSubdomain(same(subdomain));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(subdomain), eq(100));
        verify(mockAttributeValidator).isNotNull(same(env));
        verify(mockTenantRepository).findBySubdomain(same(subdomain));
        verify(mockIdentityService).getIdentity(same(mockServletRequest));
        verify(mockIdentityRepository).findById(same(identityId));
        verify(mockTenantInstantiator).instantiate(same(request));
        verify(mockTenantRepository).save(same(entity));
        verify(mockResponseMapper).map(same(entity));
        verify(mockLoginDescriptorInstantiator).instantiate(same(entity));
    }

    @Test
    public void test_duplicateSubdomain_ExceptionThrown() {
        final var identityResponse = new IdentityResponse();
        final var identityId = "the id";
        identityResponse.setId(identityId);
        final var subdomain = "the-s9ubdomain";
        final var env = Environment.DEVELOPMENT;
        final var request = new CreateTenantRequest();
        request.setSubdomain(subdomain);
        request.setEnvironment(env);
        final var entity = new TenantEntity();

        when(mockAttributeValidator.isValidSubdomain(same(subdomain))).thenCallRealMethod();
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(same(subdomain), eq(100))).thenCallRealMethod();
        when(mockAttributeValidator.isNotNull(same(env))).thenCallRealMethod();
        when(mockTenantRepository.findBySubdomain(same(subdomain))).thenReturn(Optional.of(entity));

        final var exception = assertThrows(DuplicateResourceException.class, () -> subject.create(mockServletRequest, request));

        verify(mockAttributeValidator).isValidSubdomain(same(subdomain));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(subdomain), eq(100));
        verify(mockAttributeValidator).isNotNull(same(env));
        verify(mockTenantRepository).findBySubdomain(same(subdomain));
        assertThat(exception.getMessage(), is(equalTo("duplicate subdomain: " + subdomain)));
    }

    @Test
    public void test_IdentityNotFound_ExceptionThrown() {
        final var identityResponse = new IdentityResponse();
        final var identityId = "the id";
        identityResponse.setId(identityId);
        final var subdomain = "the-s9ubdomain";
        final var env = Environment.DEVELOPMENT;
        final var request = new CreateTenantRequest();
        request.setSubdomain(subdomain);
        request.setEnvironment(env);

        when(mockAttributeValidator.isValidSubdomain(same(subdomain))).thenCallRealMethod();
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(same(subdomain), eq(100))).thenCallRealMethod();
        when(mockAttributeValidator.isNotNull(same(env))).thenCallRealMethod();
        when(mockTenantRepository.findBySubdomain(same(subdomain))).thenReturn(Optional.empty());
        when(mockIdentityService.getIdentity(same(mockServletRequest))).thenReturn(identityResponse);
        when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.empty());

        final var exception = assertThrows(ResourceNotFoundException.class, () -> subject.create(mockServletRequest, request));

        verify(mockAttributeValidator).isValidSubdomain(same(subdomain));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(subdomain), eq(100));
        verify(mockAttributeValidator).isNotNull(same(env));
        verify(mockTenantRepository).findBySubdomain(same(subdomain));
        verify(mockIdentityService).getIdentity(same(mockServletRequest));
        verify(mockIdentityRepository).findById(same(identityId));
        assertThat(exception.getMessage(), is(equalTo("identity not found for id: " + identityId)));
    }
}
