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
import static org.hamcrest.Matchers.is;
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
import software.iridium.api.base.error.DuplicateResourceException;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.handler.NewIdentityEventHandler;
import software.iridium.api.instantiator.AuthenticationRequestInstantiator;
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
  @Mock private NewIdentityEventHandler mockEventHandler;
  @Mock private IdentityEmailEntityRepository mockEmailRepository;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private ServletTokenExtractor mockTokenExtractor;
  @Mock private AttributeValidator mockAttributeValidator;
  @Mock private TenantEntityRepository mockTenantRepository;
  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @Mock private AuthenticationRequestInstantiator mockRequestInstantiator;
  @Mock private AuthenticationService mockAuthenticationService;
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
        mockEventHandler,
        mockServletRequest,
        mockTokenExtractor,
        mockAttributeValidator,
        mockTenantRepository,
        mockApplicationRepository,
        mockRequestInstantiator,
        mockAuthenticationService,
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
  public void create_AllGood_BehavesAsExpected() {
    final var emailAddress = "you@nowehere.com";
    final var clientId = "theClientId";
    final var password = "the password";
    final var request = new CreateIdentityRequest();
    request.setUsername(emailAddress);
    request.setClientId(clientId);
    request.setPassword(password);
    final var entity = new IdentityEntity();
    final var encodedTempPassword = "ec0d3dm3";
    final var response = new IdentityResponse();
    final var tenantId = "the tenantId";
    final var tenant = new TenantEntity();

    final var authenticationRequest = new AuthenticationRequest();
    final var requestParams = new HashMap<String, String>();
    final var authenticationResponse = new AuthenticationResponse();
    final var application = new ApplicationEntity();
    application.setClientId(clientId);
    application.setTenantId(tenantId);
    final var sessionDetails = new IdentityCreateSessionDetails();

    when(mockEncoder.encode(same(password))).thenReturn(encodedTempPassword);
    when(mockIdentityInstantiator.instantiate(
            same(request), same(encodedTempPassword), same(tenantId)))
        .thenReturn(entity);
    when(mockIdentityRepository.save(entity)).thenReturn(entity);
    when(mockResponseMapper.map(same(entity), same(authenticationResponse))).thenReturn(response);
    when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
    when(mockRequestInstantiator.instantiate(same(request))).thenReturn(authenticationRequest);
    when(mockAuthenticationService.authenticate(same(authenticationRequest), same(requestParams)))
        .thenReturn(authenticationResponse);
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(emailAddress), same(tenantId)))
        .thenReturn(Optional.empty());
    when(mockRequestDetailsInstantiator.instantiate(same(requestParams), same(entity)))
        .thenReturn(sessionDetails);

    subject.create(request, requestParams);

    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId));
    verify(mockEncoder).encode(same(password));
    verify(mockIdentityInstantiator)
        .instantiate(same(request), same(encodedTempPassword), same(tenantId));
    verify(mockIdentityRepository).save(same(entity));
    verify(mockResponseMapper).map(same(entity), same(authenticationResponse));
    verify(mockEventHandler).handleEvent(same(entity), same(clientId));
    verify(mockAttributeValidator).isNotBlank(same(clientId));
    verify(mockTenantRepository).findById(same(tenantId));
    verify(mockRequestInstantiator).instantiate(same(request));
    verify(mockAuthenticationService)
        .authenticate(same(authenticationRequest), same(requestParams));
    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId));
    verify(mockRequestDetailsInstantiator).instantiate(same(requestParams), same(entity));
  }

  @Test
  public void create_DuplicateUser_ExceptionThrown() {
    final var emailAddress = "you@nowehere.com";
    final var clientId = "theClientId";
    final var password = "the password";
    final var request = new CreateIdentityRequest();
    request.setUsername(emailAddress);
    request.setClientId(clientId);
    request.setPassword(password);
    final var tenantId = "the tenantId";
    final var tenant = new TenantEntity();
    final var application = new ApplicationEntity();
    final var requestParams = new HashMap<String, String>();
    application.setTenantId(tenantId);
    final var email = new IdentityEmailEntity();

    when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(emailAddress), same(tenantId)))
        .thenReturn(Optional.of(email));

    final var exception =
        assertThrows(
            DuplicateResourceException.class, () -> subject.create(request, requestParams));

    verify(mockAttributeValidator).isNotBlank(same(clientId));
    verify(mockTenantRepository).findById(same(tenantId));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(emailAddress), same(tenantId));

    assertThat(
        exception.getMessage(),
        is(equalTo("Account already registered with: you@nowehere.com in tenant: " + tenantId)));
  }

  @Test
  public void create_ApplicationNotfoundByClientId_ExceptionThrown() {
    final var emailAddress = "you@nowehere.com";
    final var clientId = "the-client-id";
    final var request = new CreateIdentityRequest();
    request.setUsername(emailAddress);
    request.setClientId(clientId);
    final var entity = new IdentityEntity();
    final var emailEntity = new IdentityEmailEntity();
    emailEntity.setIdentity(entity);

    final var paramMap = new HashMap<String, String>();

    when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(ResourceNotFoundException.class, () -> subject.create(request, paramMap));

    assertThat(
        exception.getMessage(), is(equalTo("application not found for clientId: " + clientId)));
    verify(mockAttributeValidator).isNotBlank(same(clientId));
    verify(mockApplicationRepository).findByClientId(same(clientId));
  }

  @Test
  public void create_TenantNotFound_ExceptionThrown() {
    final var emailAddress = "you@nowehere.com";
    final var clientId = "the-client-id";
    final var request = new CreateIdentityRequest();
    request.setUsername(emailAddress);
    request.setClientId(clientId);
    final var entity = new IdentityEntity();
    final var emailEntity = new IdentityEmailEntity();
    emailEntity.setIdentity(entity);
    final var application = new ApplicationEntity();
    final var tenantId = "the-tenantId";
    application.setTenantId(tenantId);

    final var paramMap = new HashMap<String, String>();

    when(mockAttributeValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(ResourceNotFoundException.class, () -> subject.create(request, paramMap));

    assertThat(exception.getMessage(), is(equalTo("tenant not found for id: " + tenantId)));
    verify(mockAttributeValidator).isNotBlank(same(clientId));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockTenantRepository).findById(same(tenantId));
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
    final var identityList = new ArrayList<IdentityEntity>();
    identityList.add(new IdentityEntity());

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
