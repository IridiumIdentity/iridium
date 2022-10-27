/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.authentication.domain.ApplicationCreateResponse;
import software.iridium.api.authentication.domain.ApplicationSummary;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.ApplicationTypeEntity;
import software.iridium.api.entity.TenantEntity;
import software.iridium.api.instantiator.ApplicationEntityInstantiator;
import software.iridium.api.mapper.ApplicationResponseMapper;
import software.iridium.api.mapper.ApplicationSummaryMapper;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.ApplicationTypeEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private AttributeValidator mockAttributeValidator;
    @Mock
    private TenantEntityRepository mockTenantRepository;
    @Mock
    private ApplicationEntityRepository mockApplicationRepository;
    @Mock
    private ApplicationTypeEntityRepository mockApplicationTypeRepository;
    @Mock
    private ApplicationEntityInstantiator mockEntityInstantiator;
    @Mock
    private ApplicationResponseMapper mockResponseMapper;
    @Mock
    private ApplicationSummaryMapper mockSummaryMapper;
    @InjectMocks
    private ApplicationService subject;

    @AfterEach
    public void ensureNoUnexpectedMockInteractions() {
        Mockito.verifyNoMoreInteractions(mockAttributeValidator,
                mockTenantRepository,
                mockApplicationRepository,
                mockApplicationTypeRepository,
                mockEntityInstantiator,
                mockResponseMapper,
                mockSummaryMapper);
    }

    @Test
    public void create_AllGood_BehavesAsExpected() {
        final var tenantId = "the tenant id";
        final var applicationTypeId = "app type id";
        final var name = "app name";
        final var applicationType = new ApplicationTypeEntity();
        final var tenant = new TenantEntity();
        final var request = new ApplicationCreateRequest();
        request.setApplicationTypeId(applicationTypeId);
        request.setName(name);
        final var entity = new ApplicationEntity();
        final var response = new ApplicationCreateResponse();

        when(mockAttributeValidator.isUuid(anyString())).thenReturn(true);
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();
        when(mockApplicationTypeRepository.findById(same(applicationTypeId))).thenReturn(Optional.of(applicationType));
        when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
        when(mockApplicationRepository.findByNameAndTenantId(same(name), same(tenantId))).thenReturn(Optional.empty());
        when(mockEntityInstantiator.instantiate(same(request), same(applicationType), same(tenantId))).thenReturn(entity);
        when(mockResponseMapper.map(same(entity))).thenReturn(response);
        when(mockApplicationRepository.save(same(entity))).thenReturn(entity);

        assertThat(subject.create(request, tenantId), sameInstance(response));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(name), eq(100));
        verify(mockApplicationTypeRepository).findById(same(applicationTypeId));
        verify(mockTenantRepository).findById(same(tenantId));
        verify(mockApplicationRepository).findByNameAndTenantId(same(name), same(tenantId));
        verify(mockEntityInstantiator).instantiate(same(request), same(applicationType), same(tenantId));
        verify(mockResponseMapper).map(same(entity));
        verify(mockApplicationRepository).save(same(entity));
    }

    @Test
    public void create_InvalidApplicationTypeId_ExceptionThrown() {
        final var tenantId = UUID.randomUUID().toString();
        final var applicationTypeId = "app type id";
        final var name = "app name";
        final var request = new ApplicationCreateRequest();
        request.setName(name);
        request.setApplicationTypeId(applicationTypeId);

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.create(request, tenantId));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(name), eq(100));
        assertThat(exception.getMessage(), is(equalTo("applicationTypeId must be a properly formatted uuid: " + applicationTypeId)));
    }

    @Test
    public void create_NameIsBlank_ExceptionThrown() {
        final var tenantId = UUID.randomUUID().toString();
        final var name = "";
        final var request = new ApplicationCreateRequest();
        request.setName(name);

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.create(request, tenantId));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(name), eq(100));
        assertThat(exception.getMessage(), is(equalTo("tenant name must not be blank and less than 100 characters")));
    }

    @Test
    public void create_NameTooLong_ExceptionThrown() {
        final var tenantId = UUID.randomUUID().toString();
        final var name = "llkdfldsfldksjfkdsfkdsjlfjdslkfdskfjdsfjldsfdsfdsfldlclds" +
                "dslfjdsfljdslfdsjflsdlfkdslfdsfdskfdsfldsfdsjfldsjfldlfdskfdslkfdskfn" +
                "sdlfdslfdslfdslfldjfldsfldsflkdsfdslkfdslkfdslfldskfldsfdksfdlsfdskfds" +
                "sdlfjdslfdslfdlsfldskfjdsfdslfkdsfldskfldsknfldskjfldsjlfkjdslkfdslkfjldsjf" +
                "dlsfjdslfjdlsfldsjfldsjflfdlsfjdlsnflksdfldsjfldskjfldsflkdsjflkdsjlkfjdslfjds" +
                "sdlfsdlfdlfdlsjvlkdslfjldsjfldsjlfjdslfdslfdlkfldsfjldsfjdslfdlfldskfldsfd" +
                "sdlfjlsdfldsfdlsfjdlsfjdlsfldsfkdsjfldfjdljfldsjfldsjfldskfjlfdlfkfdslfjdslfd" +
                "lsdfldsfldslkfdslfjdslfjldsfjldsfjdlsfdsjlfdjsldfldsjfldskjfldsjfldsfjldskfjdlsf";
        final var request = new ApplicationCreateRequest();
        request.setName(name);

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();

        final var exception = assertThrows(IllegalArgumentException.class, () -> subject.create(request, tenantId));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(name), eq(100));
        assertThat(exception.getMessage(), is(equalTo("tenant name must not be blank and less than 100 characters")));
    }

    @Test
    public void create_ApplicationTypeNotFound_ExceptionThrown() {
        final var tenant = "the tenant id";
        final var applicationTypeId = "app type id";
        final var name = "app name";
        final var request = new ApplicationCreateRequest();
        request.setApplicationTypeId(applicationTypeId);
        request.setName(name);

        when(mockAttributeValidator.isUuid(anyString())).thenReturn(true);
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();
        when(mockApplicationTypeRepository.findById(same(applicationTypeId))).thenReturn(Optional.empty());

        final var exception = assertThrows( ResourceNotFoundException.class, () -> subject.create(request, tenant));

        assertThat(exception.getMessage(), is(equalTo("application type not found for id: " + applicationTypeId)));

        verify(mockAttributeValidator).isUuid(same(tenant));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockApplicationTypeRepository).findById(same(applicationTypeId));
    }

    @Test
    public void create_TenantNotFound_ExceptionThrown() {
        final var tenantId = "the tenant id";
        final var applicationTypeId = "app type id";
        final var name = "app name";
        final var request = new ApplicationCreateRequest();
        request.setApplicationTypeId(applicationTypeId);
        request.setName(name);
        final var applicationType = new ApplicationTypeEntity();

        when(mockAttributeValidator.isUuid(anyString())).thenReturn(true);
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();
        when(mockApplicationTypeRepository.findById(same(applicationTypeId))).thenReturn(Optional.of(applicationType));
        when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.empty());

        final var exception = assertThrows(ResourceNotFoundException.class, () -> subject.create(request, tenantId));

        assertThat(exception.getMessage(), is(equalTo("tenant not found for id: " + tenantId)));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockApplicationTypeRepository).findById(same(applicationTypeId));
        verify(mockTenantRepository).findById(same(tenantId));
    }

    @Test
    public void create_DuplicateApplicationName_ExceptionThrown() {
        final var tenantId = "the tenant id";
        final var applicationTypeId = "app type id";
        final var name = "app name";
        final var request = new ApplicationCreateRequest();
        request.setApplicationTypeId(applicationTypeId);
        request.setName(name);
        final var applicationType = new ApplicationTypeEntity();
        final var tenant = new TenantEntity();
        final var entity = new ApplicationEntity();

        when(mockAttributeValidator.isUuid(anyString())).thenReturn(true);
        when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt())).thenCallRealMethod();
        when(mockApplicationTypeRepository.findById(same(applicationTypeId))).thenReturn(Optional.of(applicationType));
        when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
        when(mockApplicationRepository.findByNameAndTenantId(same(name), same(tenantId))).thenReturn(Optional.of(entity));

        final var exception = assertThrows(DuplicateResourceException.class, () -> subject.create(request, tenantId));

        assertThat(exception.getMessage(), is(equalTo(String.format("duplicate application name: %s for tenant %s", request.getName(), tenantId))));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockApplicationTypeRepository).findById(same(applicationTypeId));
        verify(mockTenantRepository).findById(same(tenantId));
        verify(mockApplicationRepository).findByNameAndTenantId(same(name), same(tenantId));
    }

    @Test
    public void getPageByTenantIdAndApplicationTypeId_AllGood_BehavesAsExpected() {
        final var tenantId = UUID.randomUUID().toString();
        final var applicationTypeId = UUID.randomUUID().toString();
        final var page = 1;
        final var size = 3;
        final var active = true;
        final var applications = new ArrayList<ApplicationEntity>();
        applications.add(new ApplicationEntity());
        final var pageOfApplications = new PageImpl<>(applications);
        final var summaries = new ArrayList<ApplicationSummary>();

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
        when(mockAttributeValidator.isPositive(anyInt())).thenCallRealMethod();
        when(mockAttributeValidator.isNotNull(anyBoolean())).thenCallRealMethod();
        when(mockApplicationRepository.findAllByTenantIdAndApplicationTypeIdAndActive(same(tenantId), same(applicationTypeId), same(active), any(PageRequest.class))).thenReturn(pageOfApplications);
        when(mockSummaryMapper.mapToSummaries(any())).thenReturn(summaries);

        final var response = subject.getPageByTenantIdAndApplicationTypeId(tenantId, applicationTypeId, page, size, active);

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockAttributeValidator).isPositive(same(page));
        verify(mockAttributeValidator).isPositive(same(size));
        verify(mockAttributeValidator).isNotNull(same(active));
        verify(mockApplicationRepository).findAllByTenantIdAndApplicationTypeIdAndActive(same(tenantId), same(applicationTypeId), same(active), any(PageRequest.class));
        verify(mockSummaryMapper).mapToSummaries(eq(applications));
        assertThat(response.getPageInfo().getCount(), is(equalTo(1)));
        assertThat(response.getPageInfo().getPage(), is(equalTo(page)));
        assertThat(response.getData().size(), is(equalTo(summaries.size())));
    }

    @Test
    public void getPageByTenantIdAndApplicationTypeId_ActiveIsNull_ExceptionThrown() {
        final var tenantId = UUID.randomUUID().toString();
        final var applicationTypeId = UUID.randomUUID().toString();
        final var page = 1;
        final var size = 3;
        final Boolean active = null;

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
        when(mockAttributeValidator.isPositive(anyInt())).thenCallRealMethod();
        when(mockAttributeValidator.isNotNull(nullable(Boolean.class))).thenCallRealMethod();

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () ->subject.getPageByTenantIdAndApplicationTypeId(tenantId, applicationTypeId, page, size, null));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockAttributeValidator).isPositive(same(page));
        verify(mockAttributeValidator).isPositive(same(size));
        verify(mockAttributeValidator).isNotNull(nullable(Boolean.class));
        assertThat(exception.getMessage(), is(equalTo("active must be either true or false: " + active)));
    }

    @Test
    public void getPageByTenantIdAndApplicationTypeId_PageSizeIsNegativeNumber_ExceptionThrown() {
        final var tenantId = UUID.randomUUID().toString();
        final var applicationTypeId = UUID.randomUUID().toString();
        final var page = 1;
        final var size = -3;

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
        when(mockAttributeValidator.isPositive(anyInt())).thenCallRealMethod();

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () ->subject.getPageByTenantIdAndApplicationTypeId(tenantId, applicationTypeId, page, size, null));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockAttributeValidator).isPositive(same(page));
        verify(mockAttributeValidator).isPositive(same(size));
        assertThat(exception.getMessage(), is(equalTo("size must be a positive integer: " + size)));
    }

    @Test
    public void getPageByTenantIdAndApplicationTypeId_PageIsNegativeNumber_ExceptionThrown() {
        final var tenantId = UUID.randomUUID().toString();
        final var applicationTypeId = UUID.randomUUID().toString();
        final var page = -1;

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
        when(mockAttributeValidator.isPositive(anyInt())).thenCallRealMethod();

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () ->subject.getPageByTenantIdAndApplicationTypeId(tenantId, applicationTypeId, page, null, null));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        verify(mockAttributeValidator).isPositive(same(page));
        assertThat(exception.getMessage(), is(equalTo("page must be a positive integer: " + page)));
    }

    @Test
    public void getPageByTenantIdAndApplicationTypeId_ApplicationTypeIdIsInvalid_ExceptionThrown() {
        final var tenantId = UUID.randomUUID().toString();
        final var applicationTypeId = "not a uuid";

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () ->subject.getPageByTenantIdAndApplicationTypeId(tenantId, applicationTypeId, null, null, null));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        verify(mockAttributeValidator).isUuid(same(applicationTypeId));
        assertThat(exception.getMessage(), is(equalTo("applicationTypeId must be a valid uuid: " + applicationTypeId)));
    }

    @Test
    public void getPageByTenantIdAndApplicationTypeId_TenantIdIsInvalid_ExceptionThrown() {
        final var tenantId = "invalid";

        when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();

        final var exception = assertThrows(
                IllegalArgumentException.class,
                () ->subject.getPageByTenantIdAndApplicationTypeId(tenantId, null, null, null, null));

        verify(mockAttributeValidator).isUuid(same(tenantId));
        assertThat(exception.getMessage(), is(equalTo("tenantId must be a valid uuid: " + tenantId)));
    }
}
