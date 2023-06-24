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

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.iridium.api.authentication.domain.*;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.instantiator.IdentityCreateRequestDetailsInstantiator;
import software.iridium.api.instantiator.IdentityEntityInstantiator;
import software.iridium.api.mapper.IdentityEntityMapper;
import software.iridium.api.mapper.IdentityResponseMapper;
import software.iridium.api.mapper.IdentitySummaryResponseMapper;
import software.iridium.api.repository.*;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.ServletTokenExtractor;
import software.iridium.entity.*;

@ExtendWith(MockitoExtension.class)
class IdentityServiceTest {

  @Mock private AuthenticationEntityRepository mockAuthenticationEntityRepository;
  @Mock private IdentityEntityMapper mockIdentityEntityMapper;
  @Mock private IdentityEntityInstantiator mockIdentityInstantiator;
  @Mock private IdentityEntityRepository mockIdentityRepository;
  @Mock private IdentityResponseMapper mockResponseMapper;
  @Mock private BCryptPasswordEncoder mockEncoder;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private ServletTokenExtractor mockTokenExtractor;
  @Mock private AttributeValidator mockAttributeValidator;
  @Mock private TenantEntityRepository mockTenantRepository;
  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @Mock private IdentityCreateRequestDetailsInstantiator mockRequestDetailsInstantiator;
  @Mock private IdentitySummaryResponseMapper mockSummaryMapper;
  @Mock private AccessTokenEntityRepository accessTokenRepository;
  @InjectMocks private IdentityService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockAuthenticationEntityRepository,
        mockIdentityEntityMapper,
        mockIdentityInstantiator,
        mockIdentityRepository,
        mockResponseMapper,
        mockEncoder,
        mockServletRequest,
        mockTokenExtractor,
        mockAttributeValidator,
        mockTenantRepository,
        mockApplicationRepository,
        accessTokenRepository,
        mockRequestDetailsInstantiator,
        mockSummaryMapper);
  }

  @Test
  public void getIdentity_AllGood_BehavesAsExpected() {
    final var userAuthToken = "userAuthToken";
    final var identityId = "the identity id";
    final var serverName = "localhost";
    final var accessToken = new AccessTokenEntity();
    final var identity = new IdentityEntity();
    accessToken.setIdentityId(identityId);
    final var identityResponse = new IdentityResponse();

    when(accessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(userAuthToken), any(Date.class)))
        .thenReturn(Optional.of(accessToken));
    when(mockIdentityEntityMapper.map(same(identity))).thenReturn(identityResponse);
    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(userAuthToken);
    when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.of(identity));
    when(mockServletRequest.getServerName()).thenReturn(serverName);

    assertThat(subject.getIdentity(mockServletRequest), sameInstance(identityResponse));

    verify(accessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(userAuthToken), any(Date.class));
    verify(mockIdentityEntityMapper).map(same(identity));
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockIdentityRepository).findById(same(identityId));
    verify(mockServletRequest).getServerName();
  }

  @Test
  public void getIdentity_EntityNotFound_ExceptionThrown() {
    final var userAuthToken = "userAuthToken";
    final var serverName = "localhost";

    when(accessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(userAuthToken), any(Date.class)))
        .thenReturn(Optional.empty());
    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(userAuthToken);
    when(mockServletRequest.getServerName()).thenReturn(serverName);

    assertThrows(NotAuthorizedException.class, () -> subject.getIdentity(mockServletRequest));

    verify(accessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(userAuthToken), any(Date.class));
    verify(mockIdentityEntityMapper, never()).map(any());
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockServletRequest).getServerName();
  }

  @Test
  public void getPage_AllGood_BehavesAsExpected() {
    final var tenantId = UUID.randomUUID().toString();
    final var page = 1;
    final var size = 3;
    final var active = true;
    final var identityList = new ArrayList<IdentityEntity>();
    identityList.add(new IdentityEntity());
    final var identities = new PageImpl<>(identityList);
    final var summaries = new ArrayList<IdentitySummaryResponse>();

    final var userAuthToken = "userAuthToken";
    final var identityId = "the identity id";
    final var accessToken = new AccessTokenEntity();
    final var admin = new IdentityEntity();
    final var tenant = new TenantEntity();

    tenant.setId(tenantId);
    admin.getManagedTenants().add(tenant);
    accessToken.setIdentityId(identityId);

    when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.isPositive(anyInt())).thenCallRealMethod();
    when(mockAttributeValidator.isZeroOrGreater(anyInt())).thenCallRealMethod();
    when(mockAttributeValidator.isNotNull(anyBoolean())).thenCallRealMethod();
    when(accessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(userAuthToken), any(Date.class)))
        .thenReturn(Optional.of(accessToken));

    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(userAuthToken);
    when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.of(admin));
    when(mockIdentityRepository.findAllByParentTenantIdAndActive(
            same(tenantId), same(active), any(PageRequest.class)))
        .thenReturn(identities);
    when(mockSummaryMapper.mapToSummaries(any())).thenReturn(summaries);

    final var response = subject.getPage(mockServletRequest, tenantId, page, size, active);

    verify(mockAttributeValidator).isUuid(same(tenantId));
    verify(mockAttributeValidator).isZeroOrGreater(same(page));
    verify(mockAttributeValidator).isPositive(same(size));
    verify(mockAttributeValidator).isNotNull(same(active));
    verify(accessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(userAuthToken), any(Date.class));
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockIdentityRepository).findById(same(identityId));
    verify(mockIdentityRepository)
        .findAllByParentTenantIdAndActive(same(tenantId), same(active), any(PageRequest.class));
    verify(mockSummaryMapper).mapToSummaries(eq(identityList));
    assertThat(response.getPageInfo().getCount(), CoreMatchers.is(equalTo(1)));
    assertThat(response.getPageInfo().getPage(), CoreMatchers.is(equalTo(page)));
    assertThat(response.getData().size(), CoreMatchers.is(equalTo(summaries.size())));
  }

  @Test
  public void getPage_NonManagedTenant_Unauthorized() {
    final var tenantId = UUID.randomUUID().toString();
    final var page = 1;
    final var size = 3;
    final var active = true;

    final var userAuthToken = "userAuthToken";
    final var identityId = "the identity id";
    final var accessToken = new AccessTokenEntity();
    final var admin = new IdentityEntity();
    final var tenant = new TenantEntity();

    tenant.setId(tenantId);
    accessToken.setIdentityId(identityId);

    when(mockAttributeValidator.isUuid(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.isPositive(anyInt())).thenCallRealMethod();
    when(mockAttributeValidator.isZeroOrGreater(anyInt())).thenCallRealMethod();
    when(mockAttributeValidator.isNotNull(anyBoolean())).thenCallRealMethod();
    when(accessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(userAuthToken), any(Date.class)))
        .thenReturn(Optional.of(accessToken));

    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(userAuthToken);
    when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.of(admin));

    final var exception =
        assertThrows(
            NotAuthorizedException.class,
            () -> subject.getPage(mockServletRequest, tenantId, page, size, active));

    verify(mockAttributeValidator).isUuid(same(tenantId));
    verify(mockAttributeValidator).isZeroOrGreater(same(page));
    verify(mockAttributeValidator).isPositive(same(size));
    verify(mockAttributeValidator).isNotNull(same(active));
    verify(accessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(userAuthToken), any(Date.class));
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockIdentityRepository).findById(same(identityId));
    verify(mockIdentityRepository, never())
        .findAllByParentTenantIdAndActive(anyString(), anyBoolean(), any(PageRequest.class));
    verify(mockSummaryMapper, never()).mapToSummaries(anyList());
  }
}
