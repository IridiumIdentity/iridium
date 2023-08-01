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
package software.iridium.api.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import software.iridium.api.repository.AccessTokenEntityRepository;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.repository.TenantEntityRepository;
import software.iridium.entity.AccessTokenEntity;
import software.iridium.entity.IdentityEntity;
import software.iridium.entity.TenantEntity;

@ExtendWith(MockitoExtension.class)
class TenantManagementFilterTest {

  @Mock private TenantEntityRepository mockTenantRepository;
  @Mock private AccessTokenEntityRepository mockAccessTokenRepository;
  @Mock private IdentityEntityRepository mockIdentityRepository;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private HttpServletResponse mockServletResponse;
  @InjectMocks private TenantManagementFilter subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockTenantRepository,
        mockAccessTokenRepository,
        mockIdentityRepository,
        mockServletRequest,
        mockServletResponse);
  }

  @Test
  public void verifyOwnershipClaim_AllGood_BehavesAsExpected() {

    final var tenantId = "the tenant id";
    final var identityId = "the identity id";
    final var tenant = new TenantEntity();
    tenant.setId(tenantId);
    final var authorizationHeaderValue = "Bearer someTokenValue";
    final var accessToken = new AccessTokenEntity();
    accessToken.setIdentityId(identityId);
    final var identity = new IdentityEntity();
    identity.getManagedTenants().add(tenant);

    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
    when(mockServletRequest.getHeader(eq(HttpHeaders.AUTHORIZATION)))
        .thenReturn(authorizationHeaderValue);
    when(mockAccessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            eq("someTokenValue"), any(Date.class)))
        .thenReturn(Optional.of(accessToken));
    when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.of(identity));

    assertTrue(subject.verifyOwnershipClaim(mockServletRequest, tenantId));

    verify(mockTenantRepository).findById(same(tenantId));
    verify(mockServletRequest).getHeader(eq(HttpHeaders.AUTHORIZATION));
    verify(mockAccessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(eq("someTokenValue"), any(Date.class));
    verify(mockIdentityRepository).findById(same(identityId));
  }
}
