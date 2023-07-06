/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderRequest;
import software.iridium.api.authentication.domain.ExternalIdentityProviderUpdateRequest;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.instantiator.ExternalIdentityProviderInstantiator;
import software.iridium.api.mapper.CreateExternalIdentityProviderResponseMapper;
import software.iridium.api.mapper.ExternalIdentityProviderResponseMapper;
import software.iridium.api.mapper.ExternalIdentityProviderSummaryResponseMapper;
import software.iridium.api.mapper.ExternalIdentityProviderUpdateResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderEntityRepository;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.updator.ExternalIdentityProviderUpdator;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.CreateExternalIdentityProviderRequestValidator;
import software.iridium.api.validator.ExternalIdentityProviderUpdateRequestValidator;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.entity.TenantEntity;

@ExtendWith(MockitoExtension.class)
class ExternalIdentityProviderServiceTest {

  @Mock private AttributeValidator mockValidator;
  @Mock private CreateExternalIdentityProviderRequestValidator mockRequestValidator;
  @Mock private ExternalIdentityProviderTemplateEntityRepository mockTemplateRepository;
  @Mock private ExternalIdentityProviderEntityRepository mockProviderRepository;
  @Mock private TenantEntityRepository mockTenantRepository;
  @Mock private ExternalIdentityProviderInstantiator mockProviderInstantiator;
  @Mock private CreateExternalIdentityProviderResponseMapper mockCreateResponseMapper;
  @Mock private ExternalIdentityProviderSummaryResponseMapper mockSummaryMapper;
  @Mock private ExternalIdentityProviderUpdateRequestValidator mockUpdateRequestValidator;
  @Mock private ExternalIdentityProviderUpdator mockExternalIdentityProviderUpdator;
  @Mock private ExternalIdentityProviderUpdateResponseMapper mockUpdateResponseMapper;
  @Mock private ExternalIdentityProviderResponseMapper mockResponseMapper;
  @InjectMocks private ExternalIdentityProviderService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockValidator,
        mockTenantRepository,
        mockRequestValidator,
        mockTemplateRepository,
        mockProviderRepository,
        mockProviderInstantiator,
        mockCreateResponseMapper,
        mockSummaryMapper,
        mockUpdateRequestValidator,
        mockExternalIdentityProviderUpdator,
        mockUpdateResponseMapper,
        mockResponseMapper);
  }

  @Test
  public void create_AllGood_BehavesAsExpected() {
    final var tenantId = "the tenant id";
    final var templateId = "the template id";
    final var tenant = new TenantEntity();
    final var request = new CreateExternalIdentityProviderRequest();
    request.setExternalProviderTemplateId(templateId);
    final var template = new ExternalIdentityProviderTemplateEntity();
    final var provider = new ExternalIdentityProviderEntity();

    when(mockValidator.isUuid(same(tenantId))).thenReturn(true);
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
    when(mockTemplateRepository.findById(same(templateId))).thenReturn(Optional.of(template));
    when(mockProviderInstantiator.instantiate(same(tenant), same(request), same(template)))
        .thenReturn(provider);
    when(mockProviderRepository.save(same(provider))).thenReturn(provider);

    subject.create(tenantId, request);

    verify(mockValidator).isUuid(same(tenantId));
    verify(mockRequestValidator).validate(same(request));
    verify(mockTemplateRepository).findById(same(templateId));
    verify(mockProviderInstantiator).instantiate(same(tenant), same(request), same(template));
    verify(mockCreateResponseMapper).map(same(provider));
    verify(mockTenantRepository).findById(same(tenantId));
  }

  @Test
  public void retrieveAllSummaries_AllGood_BehavesAsExpected() {
    final var tenantId = "the tenant id";
    final var entities = new ArrayList<ExternalIdentityProviderEntity>();

    when(mockValidator.isUuid(same(tenantId))).thenReturn(true);
    when(mockProviderRepository.findAll()).thenReturn(entities);

    subject.retrieveAllSummaries(tenantId);

    verify(mockValidator).isUuid(same(tenantId));
    verify(mockProviderRepository).findAll();
    verify(mockSummaryMapper).mapList(same(entities));
  }

  @Test
  public void update_AllGood_BehavesAsExpected() {
    final var tenantId = "the tenant id";
    final var externalProviderId = "the external provider id";
    final var request = new ExternalIdentityProviderUpdateRequest();
    final var provider = new ExternalIdentityProviderEntity();
    final var tenant = new TenantEntity();
    tenant.setId(tenantId);
    provider.setTenant(tenant);

    when(mockValidator.isUuid(anyString())).thenReturn(true);
    when(mockProviderRepository.findById(same(externalProviderId)))
        .thenReturn(Optional.of(provider));
    when(mockExternalIdentityProviderUpdator.update(same(provider), same(request)))
        .thenReturn(provider);
    when(mockProviderRepository.save(same(provider))).thenReturn(provider);
    when(mockValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();

    subject.update(request, tenantId, externalProviderId);

    verify(mockValidator).doesNotEqual(same(tenantId), same(tenantId));
    verify(mockValidator).isUuid(tenantId);
    verify(mockValidator).isUuid(externalProviderId);
    verify(mockUpdateRequestValidator).validate(same(request));
    verify(mockProviderRepository).findById(same(externalProviderId));
    verify(mockProviderRepository).save(same(provider));
    verify(mockExternalIdentityProviderUpdator).update(same(provider), same(request));
    verify(mockUpdateResponseMapper).map(same(provider));
  }

  @Test
  public void update_TenantIdsDoNotMatch_BehavesAsExpected() {
    final var tenantId = "the tenant id";
    final var otherTenantId = "the other tenant id";
    final var externalProviderId = "the external provider id";
    final var request = new ExternalIdentityProviderUpdateRequest();
    final var provider = new ExternalIdentityProviderEntity();
    final var tenant = new TenantEntity();
    tenant.setId(otherTenantId);
    provider.setTenant(tenant);

    when(mockValidator.isUuid(anyString())).thenReturn(true);
    when(mockProviderRepository.findById(same(externalProviderId)))
        .thenReturn(Optional.of(provider));
    when(mockValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            NotAuthorizedException.class,
            () -> subject.update(request, tenantId, externalProviderId));

    assertThat(exception.getMessage(), is(equalTo("invalid data request")));

    verify(mockValidator).doesNotEqual(same(otherTenantId), same(tenantId));
    verify(mockValidator).isUuid(tenantId);
    verify(mockValidator).isUuid(externalProviderId);
    verify(mockUpdateRequestValidator).validate(same(request));
    verify(mockProviderRepository).findById(same(externalProviderId));
  }

  @Test
  public void get_AllGood_BehavesAsExpected() {
    final var tenantId = "the tenant id";
    final var externalProviderId = "the external provider id";
    final var entity = new ExternalIdentityProviderEntity();
    final var tenant = new TenantEntity();
    tenant.setId(tenantId);
    entity.setTenant(tenant);

    when(mockValidator.isUuid(anyString())).thenReturn(true);
    when(mockProviderRepository.findById(same(externalProviderId))).thenReturn(Optional.of(entity));
    when(mockValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();

    subject.get(tenantId, externalProviderId);

    verify(mockValidator).isUuid(tenantId);
    verify(mockValidator).isUuid(externalProviderId);
    verify(mockProviderRepository).findById(same(externalProviderId));
    verify(mockValidator).doesNotEqual(same(tenantId), same(tenantId));
    verify(mockResponseMapper).map(same(entity));
  }
}
