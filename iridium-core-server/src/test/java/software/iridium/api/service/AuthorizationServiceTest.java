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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import software.iridium.api.authentication.client.ProviderAccessTokenRequestor;
import software.iridium.api.authentication.client.ProviderProfileRequestor;
import software.iridium.api.authentication.domain.ApplicationAuthorizationFormRequest;
import software.iridium.api.authentication.domain.AuthorizationResponse;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.generator.ExternalProviderAccessTokenUrlGenerator;
import software.iridium.api.generator.RedirectUrlGenerator;
import software.iridium.api.generator.SuccessAuthorizationParameterGenerator;
import software.iridium.api.instantiator.AuthorizationCodeEntityInstantiator;
import software.iridium.api.instantiator.IdentityEntityInstantiator;
import software.iridium.api.mapper.IdentityResponseMapper;
import software.iridium.api.repository.*;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.api.util.SubdomainExtractor;
import software.iridium.api.validator.AuthorizationGrantTypeParamValidator;
import software.iridium.api.validator.AuthorizationRequestParameterValidator;
import software.iridium.entity.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

  @Mock private AttributeValidator mockAttributeValidator;
  @Mock private ExternalProviderAccessTokenUrlGenerator mockUrlGenerator;
  @Mock private ProviderAccessTokenRequestor mockAccessTokenRequestor;
  @Mock private IdentityEntityRepository mockIdentityRepository;
  @Mock private IdentityResponseMapper mockIdentityResponseMapper;
  @Mock private IdentityEntityInstantiator mockIdentityInstantiator;
  @Mock private IdentityEntity mockIdentity;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private AuthorizationGrantTypeParamValidator mockGrantTypeValidator;
  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @Mock private AuthorizationRequestParameterValidator mockRequestParameterValidator;
  @Mock private AuthorizationCodeEntityInstantiator mockAuthCodeInstantiator;
  @Mock private AuthorizationCodeEntityRepository mockAuthCodeRepository;
  @Mock private SuccessAuthorizationParameterGenerator mockSuccessParamGenerator;
  @Mock private RedirectUrlGenerator mockRedirectUrlGenerator;
  @Mock private TenantEntityRepository mockTenantRepository;
  @Mock private ProviderProfileRequestor mockProviderProfileRequestor;
  @Mock private IdentityEmailEntityRepository mockEmailRepository;
  @Mock private SubdomainExtractor mockSubdomainExtractor;
  @Mock private AuthenticationEntityRepository mockAuthenticationRepository;
  @InjectMocks private AuthorizationService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockAttributeValidator,
        mockUrlGenerator,
        mockAccessTokenRequestor,
        mockIdentityRepository,
        mockIdentityResponseMapper,
        mockIdentityInstantiator,
        mockServletRequest,
        mockGrantTypeValidator,
        mockApplicationRepository,
        mockRequestParameterValidator,
        mockAuthCodeInstantiator,
        mockAuthCodeRepository,
        mockSuccessParamGenerator,
        mockRedirectUrlGenerator,
        mockProviderProfileRequestor,
        mockEmailRepository,
        mockIdentity,
        mockSubdomainExtractor,
        mockAuthenticationRepository);
  }

  @Test
  public void authorizeWithProvider_UserDoesExistAllGood_BehavesAsExpected() {
    final var code = "someCode";
    final var providerName = "github";
    final var clientId = "theClientId";
    final var state = "theRandomState";
    final var providerProfileUrl = "http://someprovider.url";
    final var githubResponseEmail = "someone@somewhere.com";
    final var providerAccessToken = "the provider access token";
    final var accessToken = "theaccessToken";
    final var authzResoonse = new AuthorizationResponse();
    authzResoonse.setAccessToken(accessToken);
    final var tenantId = "theTenantId";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    final var tenant = new TenantEntity();
    tenant.setId(tenantId);
    final var externalProvider = new ExternalIdentityProviderEntity();
    externalProvider.setName(providerName);
    externalProvider.setProfileRequestBaseUrl(providerProfileUrl);
    tenant.getExternalIdentityProviders().add(externalProvider);

    final var profileResponse = new GithubProfileResponse();
    final var externalId = "the external id";
    profileResponse.setId(externalId);
    profileResponse.setEmail(githubResponseEmail);

    final var providerUrl = "http://the-url.com";
    final var authorizationResponse = new AuthorizationResponse();
    authorizationResponse.setAccessToken(providerAccessToken);
    final var email = new IdentityEmailEntity();
    final var identity = new IdentityEntity();
    email.setIdentity(identity);

    when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt()))
        .thenCallRealMethod();
    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
    when(mockUrlGenerator.generate(same(externalProvider), same(code))).thenReturn(providerUrl);
    when(mockAccessTokenRequestor.requestAccessToken(same(providerUrl)))
        .thenReturn(authorizationResponse);
    when(mockProviderProfileRequestor.requestGithubProfile(
            same(providerProfileUrl), same(providerAccessToken)))
        .thenReturn(profileResponse);
    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(githubResponseEmail), same(tenantId)))
        .thenReturn(Optional.of(email));

    subject.completeAuthorizationWithProvider(code, providerName, clientId, state);

    verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator).isNotBlank(same(providerName));
    verify(mockAttributeValidator).isNotBlank(same(state));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockTenantRepository).findById(same(tenantId));
    verify(mockUrlGenerator).generate(same(externalProvider), same(code));
    verify(mockAccessTokenRequestor).requestAccessToken(same(providerUrl));
    verify(mockProviderProfileRequestor)
        .requestGithubProfile(same(providerProfileUrl), same(providerAccessToken));
    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(githubResponseEmail), same(tenantId));
    verify(mockIdentityResponseMapper).map(same(identity));
  }

  @Test
  public void authorizeWithProvider_UserDoesNotExistCreatesNewAllGood_BehavesAsExpected() {
    final var code = "someCode";
    final var providerName = "github";
    final var clientId = "theClientId";
    final var state = "theRandomState";
    final var providerProfileUrl = "http://someprovider.url";
    final var githubResponseEmail = "someone@somewhere.com";
    final var providerAccessToken = "the provider access token";
    final var accessToken = "theaccessToken";
    final var authzResoonse = new AuthorizationResponse();
    authzResoonse.setAccessToken(accessToken);
    final var tenantId = "theTenantId";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    final var tenant = new TenantEntity();
    tenant.setId(tenantId);
    final var externalProvider = new ExternalIdentityProviderEntity();
    externalProvider.setName(providerName);
    externalProvider.setProfileRequestBaseUrl(providerProfileUrl);
    tenant.getExternalIdentityProviders().add(externalProvider);

    final var profileResponse = new GithubProfileResponse();
    final var externalId = "the external id";
    profileResponse.setId(externalId);
    profileResponse.setEmail(githubResponseEmail);

    final var providerUrl = "http://the-url.com";
    final var authorizationResponse = new AuthorizationResponse();
    authorizationResponse.setAccessToken(providerAccessToken);
    final var authorizedApplications = new ArrayList<ApplicationEntity>();

    when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt()))
        .thenCallRealMethod();
    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.of(tenant));
    when(mockUrlGenerator.generate(same(externalProvider), same(code))).thenReturn(providerUrl);
    when(mockAccessTokenRequestor.requestAccessToken(same(providerUrl)))
        .thenReturn(authorizationResponse);
    when(mockProviderProfileRequestor.requestGithubProfile(
            same(providerProfileUrl), same(providerAccessToken)))
        .thenReturn(profileResponse);
    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(githubResponseEmail), same(tenantId)))
        .thenReturn(Optional.empty());
    when(mockIdentityInstantiator.instantiateFromGithub(
            same(profileResponse), same(externalProvider)))
        .thenReturn(mockIdentity);
    when(mockIdentity.getAuthorizedApplications()).thenReturn(authorizedApplications);
    when(mockIdentityRepository.save(same(mockIdentity))).thenReturn(mockIdentity);

    subject.completeAuthorizationWithProvider(code, providerName, clientId, state);

    verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator).isNotBlank(same(providerName));
    verify(mockAttributeValidator).isNotBlank(same(state));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockTenantRepository).findById(same(tenantId));
    verify(mockUrlGenerator).generate(same(externalProvider), same(code));
    verify(mockAccessTokenRequestor).requestAccessToken(same(providerUrl));
    verify(mockProviderProfileRequestor)
        .requestGithubProfile(same(providerProfileUrl), same(providerAccessToken));
    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(githubResponseEmail), same(tenantId));
    verify(mockIdentityResponseMapper).map(same(mockIdentity));
    verify(mockIdentity).getAuthorizedApplications();
    verify(mockIdentity).setParentTenantId(same(tenantId));
    verify(mockIdentityRepository).save(same(mockIdentity));
    verify(mockIdentityResponseMapper).map(same(mockIdentity));
  }

  @Test
  public void completeAuthorizationWithProvider_ApplicationNotFound_ExceptionThrown() {
    final var code = "someCode";
    final var providerName = "github";
    final var clientId = "theClientId";
    final var state = "theRandomState";
    final var tenantId = "the tenant id";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);

    when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt()))
        .thenCallRealMethod();
    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> subject.completeAuthorizationWithProvider(code, providerName, clientId, state));

    assertThat(
        exception.getMessage(), is(equalTo("application not found for clientId: " + clientId)));

    verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator).isNotBlank(same(providerName));
    verify(mockAttributeValidator).isNotBlank(same(state));
    verify(mockApplicationRepository).findByClientId(same(clientId));
  }

  @Test
  public void completeAuthorizationWithProvider_TenantNotFound_ExceptionThrown() {
    final var code = "someCode";
    final var providerName = "github";
    final var clientId = "theClientId";
    final var state = "theRandomState";
    final var tenantId = "TheTenantId";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);

    when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt()))
        .thenCallRealMethod();
    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockTenantRepository.findById(same(tenantId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> subject.completeAuthorizationWithProvider(code, providerName, clientId, state));

    assertThat(
        exception.getMessage(),
        is(equalTo(String.format("tenant %s not found with clientId: %s", tenantId, clientId))));

    verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator).isNotBlank(same(providerName));
    verify(mockAttributeValidator).isNotBlank(same(state));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockTenantRepository).findById(same(tenantId));
  }

  @Test
  public void completeAuthorizationWithProvider_CodeIsBlank_ExceptionThrown() {
    final var code = "";
    final var providerName = "github";
    final var clientId = "theClientId";
    final var state = "someRandomState";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.completeAuthorizationWithProvider(code, providerName, clientId, state));

    assertThat(exception.getMessage(), is(equalTo("code must be not be blank")));

    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator, never()).isNotBlank(eq(providerName));
    verify(mockAttributeValidator, never()).isNotBlankAndNoLongerThan(eq(clientId), eq(32));
    verify(mockAttributeValidator, never()).isNotBlank(eq(state));
  }

  @Test
  public void completeAuthorizationWithProvider_ProviderNameIsBlank_ExceptionThrown() {
    final var code = "someCode";
    final var providerName = "";
    final var clientId = "theClientId";
    final var state = "someRandomState";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.completeAuthorizationWithProvider(code, providerName, clientId, state));

    assertThat(exception.getMessage(), is(equalTo("providerName must be blank")));

    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator).isNotBlank(same(providerName));
    verify(mockAttributeValidator, never()).isNotBlankAndNoLongerThan(eq(clientId), eq(32));
    verify(mockAttributeValidator, never()).isNotBlank(eq(state));
  }

  @Test
  public void completeAuthorizationWithProvider_ClientIdIsIsMalformed_ExceptionThrown() {
    final var code = "someCode";
    final var providerName = "github";
    final var clientId = "theClientIdsdlkfjdlfdlfjdslkjfldskjfldkfldskjfldskjfdskf";
    final var state = "someRandomState";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt()))
        .thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.completeAuthorizationWithProvider(code, providerName, clientId, state));

    assertThat(
        exception.getMessage(),
        is(equalTo("clientId must not be blank and no longer than 32 characters")));

    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator).isNotBlank(same(providerName));
    verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
    verify(mockAttributeValidator, never()).isNotBlank(eq(state));
  }

  @Test
  public void completeAuthorizationWithProvider_stateIsBlank_ExceptionThrown() {
    final var code = "someCode";
    final var providerName = "github";
    final var clientId = "theClientIdsdlkfj";
    final var state = "";

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.isNotBlankAndNoLongerThan(anyString(), anyInt()))
        .thenCallRealMethod();

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> subject.completeAuthorizationWithProvider(code, providerName, clientId, state));

    assertThat(exception.getMessage(), is(equalTo("state must be not be blank")));

    verify(mockAttributeValidator).isNotBlank(same(code));
    verify(mockAttributeValidator).isNotBlank(same(providerName));
    verify(mockAttributeValidator).isNotBlankAndNoLongerThan(same(clientId), eq(32));
    verify(mockAttributeValidator).isNotBlank(same(state));
  }

  @Test
  public void authorize_AllGood_BehavesAsExpected() {

    final var params = new HashMap<String, String>();
    final var clientId = "the client id";
    final var userToken = "theUsertoken";
    final var redirectUri = "http://localhost:4200";
    final var requestUrl = "https://somewhere.iridium.software";
    StringBuffer requestUrlBuffer = new StringBuffer();
    requestUrlBuffer.append(requestUrl);
    final var formRequest = new ApplicationAuthorizationFormRequest();
    formRequest.setUserToken(userToken);
    params.put(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), clientId);
    params.put(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), redirectUri);
    final var tenant = new TenantEntity();
    final var authentication = new AuthenticationEntity();
    authentication.setIdentity(mockIdentity);
    final var application = new ApplicationEntity();
    application.setRedirectUri(redirectUri);
    final var authorizationCode = "the auth code";
    final var authorizationCodeEntity = new AuthorizationCodeEntity();
    authorizationCodeEntity.setAuthorizationCode(authorizationCode);
    final var paramMap = new LinkedMultiValueMap<String, String>();
    final var generatedRedirectUri = "http://localhost:4200/redirect";
    final var authorizedApplications = new ArrayList<ApplicationEntity>();

    when(mockSubdomainExtractor.extract(eq(requestUrl))).thenReturn("iridium");
    when(mockServletRequest.getRequestURL()).thenReturn(requestUrlBuffer);
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockTenantRepository.findBySubdomain(eq("iridium"))).thenReturn(Optional.of(tenant));
    when(mockAuthenticationRepository.findByAuthTokenAndExpirationAfter(
            same(userToken), any(Date.class)))
        .thenReturn(Optional.of(authentication));
    when(mockRequestParameterValidator.validateAndOptionallyRedirect(
            same(redirectUri), same(params)))
        .thenReturn("");
    when(mockIdentity.getAuthorizedApplications()).thenReturn(authorizedApplications);
    when(mockAuthCodeInstantiator.instantiate(same(mockIdentity), same(params)))
        .thenReturn(authorizationCodeEntity);
    when(mockAuthCodeRepository.save(same(authorizationCodeEntity)))
        .thenReturn(authorizationCodeEntity);
    when(mockSuccessParamGenerator.generate(same(params), same(authorizationCode)))
        .thenReturn(paramMap);
    when(mockRedirectUrlGenerator.generate(same(redirectUri), same(paramMap)))
        .thenReturn(generatedRedirectUri);
    when(mockAttributeValidator.equals(anyString(), anyString())).thenCallRealMethod();
    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var response = subject.authorize(formRequest, params, mockServletRequest);

    assertThat(response, is(equalTo(generatedRedirectUri)));
    verify(mockGrantTypeValidator).validate(same(params));
    verify(mockSubdomainExtractor).extract(eq(requestUrl));
    verify(mockTenantRepository).findBySubdomain(eq("iridium"));
    verify(mockAuthenticationRepository)
        .findByAuthTokenAndExpirationAfter(same(userToken), any(Date.class));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockRequestParameterValidator)
        .validateAndOptionallyRedirect(same(redirectUri), same(params));
    verify(mockAuthCodeInstantiator).instantiate(same(mockIdentity), same(params));
    verify(mockAuthCodeRepository).save(same(authorizationCodeEntity));
    verify(mockSuccessParamGenerator).generate(same(params), same(authorizationCode));
    verify(mockRedirectUrlGenerator).generate(same(redirectUri), same(paramMap));
    verify(mockAttributeValidator).isNotBlank("");
    verify(mockAttributeValidator).equals(same(redirectUri), same(redirectUri));
    verify(mockIdentity).getAuthorizedApplications();
    assertThat(response, is(equalTo(generatedRedirectUri)));
  }

  @Test
  public void authorize_RedirectsBecauseOfError_BehavesAsExpected() {
    final var params = new HashMap<String, String>();
    final var clientId = "the client id";
    final var redirectUri = "http://localhost:4200";
    final var requestUrl = "http://iridium.iridium.com";
    final var subdomain = "iridium";
    final var errorRedirectUri = "http://localhost:8009/error";

    params.put(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), clientId);
    params.put(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), redirectUri);
    final var application = new ApplicationEntity();
    application.setRedirectUri(redirectUri);
    final var userToken = "shortlived";
    final var urlStrBuffer = new StringBuffer(requestUrl);
    final var tenant = new TenantEntity();
    final var formRequest = new ApplicationAuthorizationFormRequest();
    formRequest.setUserToken(userToken);
    final var authentication = new AuthenticationEntity();

    when(mockServletRequest.getRequestURL()).thenReturn(urlStrBuffer);
    when(mockSubdomainExtractor.extract(eq(requestUrl))).thenReturn(subdomain);
    when(mockTenantRepository.findBySubdomain(same(subdomain))).thenReturn(Optional.of(tenant));
    when(mockAuthenticationRepository.findByAuthTokenAndExpirationAfter(
            same(userToken), any(Date.class)))
        .thenReturn(Optional.of(authentication));
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));
    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockAttributeValidator.equals(anyString(), anyString())).thenCallRealMethod();
    when(mockRequestParameterValidator.validateAndOptionallyRedirect(
            same(redirectUri), same(params)))
        .thenReturn(errorRedirectUri);

    final var response = subject.authorize(formRequest, params, mockServletRequest);

    verify(mockGrantTypeValidator).validate(same(params));
    verify(mockServletRequest).getRequestURL();
    verify(mockSubdomainExtractor).extract(eq(requestUrl));
    verify(mockTenantRepository).findBySubdomain(same(subdomain));
    verify(mockAuthenticationRepository)
        .findByAuthTokenAndExpirationAfter(same(userToken), any(Date.class));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockAttributeValidator).isNotBlank(same(redirectUri));
    verify(mockAttributeValidator).equals(same(redirectUri), same(redirectUri));
    verify(mockAttributeValidator).isNotBlank(same(errorRedirectUri));
    verify(mockRequestParameterValidator)
        .validateAndOptionallyRedirect(same(redirectUri), same(params));
    assertThat(response, is(equalTo(errorRedirectUri)));
  }

  @Test
  public void authorize_ApplicationNotFound_ExceptionThrown() {
    final var params = new HashMap<String, String>();
    final var clientId = "the client id";
    final var redirectUri = "http://localhost:4200";
    final var requestUrl = "http://iridium.iridium.com";
    final var subdomain = "iridium";

    params.put(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), clientId);
    params.put(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), redirectUri);
    final var application = new ApplicationEntity();
    application.setRedirectUri(redirectUri);
    final var userToken = "shortlived";
    final var urlStrBuffer = new StringBuffer(requestUrl);
    final var tenant = new TenantEntity();
    final var formRequest = new ApplicationAuthorizationFormRequest();
    formRequest.setUserToken(userToken);
    final var authentication = new AuthenticationEntity();

    when(mockServletRequest.getRequestURL()).thenReturn(urlStrBuffer);
    when(mockSubdomainExtractor.extract(eq(requestUrl))).thenReturn(subdomain);
    when(mockTenantRepository.findBySubdomain(same(subdomain))).thenReturn(Optional.of(tenant));
    when(mockAuthenticationRepository.findByAuthTokenAndExpirationAfter(
            same(userToken), any(Date.class)))
        .thenReturn(Optional.of(authentication));
    when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(
            ResourceNotFoundException.class,
            () -> subject.authorize(formRequest, params, mockServletRequest));

    verify(mockGrantTypeValidator).validate(same(params));
    verify(mockServletRequest).getRequestURL();
    verify(mockSubdomainExtractor).extract(eq(requestUrl));
    verify(mockTenantRepository).findBySubdomain(same(subdomain));
    verify(mockAuthenticationRepository)
        .findByAuthTokenAndExpirationAfter(same(userToken), any(Date.class));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    assertThat(
        exception.getMessage(), is(equalTo("application not found for client_id: " + clientId)));
  }
}
