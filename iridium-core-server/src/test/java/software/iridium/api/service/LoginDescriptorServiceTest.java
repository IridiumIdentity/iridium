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

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.TenantLogoUrlUpdateRequest;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.mapper.LoginDescriptorResponseMapper;
import software.iridium.api.mapper.TenantLogoUpdateResponseMapper;
import software.iridium.api.repository.LoginDescriptorEntityRepository;
import software.iridium.api.updator.TenantLogoUrlUpdator;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.validator.TenantLogoUrlUpdateRequestValidator;
import software.iridium.entity.LoginDescriptorEntity;

@ExtendWith(MockitoExtension.class)
class LoginDescriptorServiceTest {

  @Mock private LoginDescriptorEntityRepository mockLoginDescriptorRepository;
  @Mock private LoginDescriptorResponseMapper mockResponseMapper;
  @Mock private AttributeValidator mockAttributeValidator;
  @Mock private TenantLogoUrlUpdateRequestValidator mockLogoUpdateRequestValidator;
  @Mock private TenantLogoUrlUpdator mockLogoUrlUpdator;
  @Mock private TenantLogoUpdateResponseMapper mockLogoUpdateResponseMapper;
  @InjectMocks private LoginDescriptorService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockLoginDescriptorRepository,
        mockResponseMapper,
        mockAttributeValidator,
        mockLogoUpdateRequestValidator,
        mockLogoUrlUpdator,
        mockLogoUpdateResponseMapper);
  }

  @Test
  public void getBySubdomain_AllGood_BehavesAsExpected() {
    final var subdomain = "subdomain";
    final var loginDescriptor = new LoginDescriptorEntity();

    when(mockAttributeValidator.isValidSubdomain(same(subdomain))).thenReturn(true);
    when(mockLoginDescriptorRepository.findByTenant_Subdomain(same(subdomain)))
        .thenReturn(Optional.of(loginDescriptor));

    subject.getBySubdomain(subdomain);

    verify(mockAttributeValidator).isValidSubdomain(same(subdomain));
    verify(mockLoginDescriptorRepository).findByTenant_Subdomain(same(subdomain));
    verify(mockResponseMapper).map(same(loginDescriptor));
  }

  @Test
  public void getBySubdomain_LoginDescriptorNotFound_ExceptionThrown() {
    final var subdomain = "subdomain";

    when(mockAttributeValidator.isValidSubdomain(same(subdomain))).thenReturn(true);
    when(mockLoginDescriptorRepository.findByTenant_Subdomain(same(subdomain)))
        .thenReturn(Optional.empty());

    final var exception =
        assertThrows(ResourceNotFoundException.class, () -> subject.getBySubdomain(subdomain));

    verify(mockAttributeValidator).isValidSubdomain(same(subdomain));
    verify(mockLoginDescriptorRepository).findByTenant_Subdomain(same(subdomain));
    assertThat(
        exception.getMessage(),
        is(equalTo("login descriptor not found for subdomain: " + subdomain)));
  }

  @Test
  public void updateLogoURL_AllGood_BehavesAsExpected() {
    final var request = new TenantLogoUrlUpdateRequest();
    final var tenantId = "the tenant id";
    final var entity = new LoginDescriptorEntity();

    when(mockLoginDescriptorRepository.findByTenantId(same(tenantId)))
        .thenReturn(Optional.of(entity));
    when(mockLogoUrlUpdator.update(same(entity), same(request))).thenReturn(entity);
    when(mockLoginDescriptorRepository.save(same(entity))).thenReturn(entity);
    when(mockAttributeValidator.isUuid(anyString())).thenReturn(true);

    subject.updateLogoURL(request, tenantId);

    verify(mockAttributeValidator).isUuid(same(tenantId));
    verify(mockLogoUpdateRequestValidator).validate(same(request));
    verify(mockLoginDescriptorRepository).findByTenantId(same(tenantId));
    verify(mockLogoUrlUpdator).update(same(entity), same(request));
    verify(mockLoginDescriptorRepository).save(same(entity));
    verify(mockLogoUpdateResponseMapper).map(same(entity));
  }

  @Test
  public void getByTenantId_AllGood_BehavesAsExpected() {
    final var tenantId = "tenant id";
    final var loginDescriptor = new LoginDescriptorEntity();

    when(mockAttributeValidator.isUuid(same(tenantId))).thenReturn(true);
    when(mockLoginDescriptorRepository.findByTenantId(same(tenantId)))
            .thenReturn(Optional.of(loginDescriptor));

    subject.getByTenantId(tenantId);

    verify(mockAttributeValidator).isUuid(same(tenantId));
    verify(mockLoginDescriptorRepository).findByTenantId(same(tenantId));
    verify(mockResponseMapper).map(same(loginDescriptor));
  }

  @Test
  public void getByTenantId_LoginDescriptorNotFound_ExceptionThrown() {
    final var tenantId = "tenant id";

    when(mockAttributeValidator.isUuid(same(tenantId))).thenReturn(true);
    when(mockLoginDescriptorRepository.findByTenantId(same(tenantId)))
            .thenReturn(Optional.empty());

    final var exception =
            assertThrows(ResourceNotFoundException.class, () -> subject.getByTenantId(tenantId));

    verify(mockAttributeValidator).isUuid(same(tenantId));
    verify(mockLoginDescriptorRepository).findByTenantId(same(tenantId));
    assertThat(
            exception.getMessage(),
            is(equalTo("login descriptor not found for tenant id")));
  }
}
