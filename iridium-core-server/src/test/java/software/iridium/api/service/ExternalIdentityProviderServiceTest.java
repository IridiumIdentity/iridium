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

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderRequest;
import software.iridium.api.instantiator.ExternalIdentityProviderInstantiator;
import software.iridium.api.mapper.CreateExternalIdentityProviderResponseMapper;
import software.iridium.api.repository.ExternalIdentityProviderEntityRepository;
import software.iridium.api.repository.ExternalIdentityProviderTemplateEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.CreateExternalIdentityProviderRequestValidator;
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
  @Mock private CreateExternalIdentityProviderResponseMapper mockResponseMapper;
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
    verify(mockResponseMapper).map(same(provider));
    verify(mockTenantRepository).findById(same(tenantId));
  }
}
