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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.mapper.ProviderTemplateSummaryResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.entity.ExternalIdentityProviderEntity;
import software.iridium.entity.ExternalIdentityProviderTemplateEntity;
import software.iridium.entity.TenantEntity;

@ExtendWith(MockitoExtension.class)
class ExternalIdentityProviderTemplateServiceTest {

  @Mock private ExternalIdentityProviderTemplateEntityRepository mockTemplateRepository;
  @Mock private ProviderTemplateSummaryResponseMapper mockResponseMapper;
  @Mock private AttributeValidator mockValidator;
  @Mock private TenantEntityRepository mockTenantRepository;
  @InjectMocks private ExternalIdentityProviderTemplateService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockTemplateRepository, mockResponseMapper, mockValidator, mockTenantRepository);
  }

  @Test
  public void retrieveAllSummaries_AllGood_BehavesAsExpected() {
    final List<ExternalIdentityProviderTemplateEntity> entities = new ArrayList<>();

    when(mockTemplateRepository.findAll()).thenReturn(entities);

    subject.retrieveAllSummaries();

    verify(mockTemplateRepository).findAll();
    verify(mockResponseMapper).mapList(same(entities));
  }

  @Test
  public void retrieveAvailableSummaries_AllGood_BehavesAsExpected() {
    final var tenantId = "the tenant id";
    final var template1Id = "the template 1 id";
    final var template2Id = "the template 2 id";

    final var templates = new ArrayList<ExternalIdentityProviderTemplateEntity>();
    final var template1 = new ExternalIdentityProviderTemplateEntity();
    template1.setId(template1Id);
    final var template2 = new ExternalIdentityProviderTemplateEntity();
    template2.setId(template2Id);
    templates.add(template1);
    templates.add(template2);
    final var tenant = new TenantEntity();
    final var providers = new ArrayList<ExternalIdentityProviderEntity>();
    final var provider = new ExternalIdentityProviderEntity();
    provider.setTemplate(template1);
    providers.add(provider);
    tenant.setExternalIdentityProviders(providers);

    when(mockValidator.isUuid(same(tenantId))).thenReturn(true);
    when(mockTemplateRepository.findAll()).thenReturn(templates);
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));

    final var summaries = subject.retrieveAvailableSummaries(tenantId);

    verify(mockValidator).isUuid(same(tenantId));
    verify(mockTemplateRepository).findAll();
    verify(mockTenantRepository).findById(same(tenantId));
    verify(mockResponseMapper).mapList(anyList());
  }
}
