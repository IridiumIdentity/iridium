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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.repository.AccessTokenEntityRepository;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.AuthenticationEntityRepository;
import software.iridium.api.repository.IdentityEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.ServletTokenExtractor;
import software.iridium.entity.AccessTokenEntity;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.AuthenticationEntity;
import software.iridium.entity.IdentityEntity;

@ExtendWith(MockitoExtension.class)
class TokenManagerTest {

  @Mock private AuthenticationEntityRepository mockAuthenticationEntityRepository;
  @Mock private AuthenticationGenerator mockAuthenticationGenerator;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private IdentityEntityRepository mockIdentityRepository;
  @Mock private AttributeValidator mockValidator;
  @Mock private AccessTokenEntityRepository mockAccessTokenRepository;
  @Mock private ServletTokenExtractor mockTokenExtractor;
  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @InjectMocks private TokenManager subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockAuthenticationEntityRepository,
        mockAuthenticationGenerator,
        mockServletRequest,
        mockIdentityRepository,
        mockValidator,
        mockAccessTokenRepository,
        mockTokenExtractor,
        mockApplicationRepository);
  }

  @Test
  public void getOrGenerateToken_AuthenticationFound_NewestTokenExpired_BehavesAsExpected() {
    final var identityId = "theId";
    final var identity = new IdentityEntity();
    identity.setId(identityId);
    final var authId = "authId";
    final var auth = new AuthenticationEntity();
    auth.setId(authId);
    final var currentDate = Calendar.getInstance().getTime();
    final var thirtyMinBehind = DateUtils.addMinutes(currentDate, -30);
    auth.setExpiration(thirtyMinBehind);
    final var generatedAuth = new AuthenticationEntity();
    final var authToken = "auth token";
    final var refreshToken = "the refresh token";
    generatedAuth.setAuthToken(authToken);
    generatedAuth.setRefreshToken(refreshToken);

    when(mockAuthenticationEntityRepository.findFirstByIdentityIdOrderByCreatedDesc(
            same(identityId)))
        .thenReturn(Optional.of(auth));
    when(mockAuthenticationGenerator.generateAuthentication(same(identity)))
        .thenReturn(generatedAuth);

    final var pair = subject.getOrGenerateToken(identity);

    verify(mockAuthenticationEntityRepository)
        .findFirstByIdentityIdOrderByCreatedDesc(same(identityId));
    verify(mockAuthenticationGenerator).generateAuthentication(same(identity));
    verify(mockAuthenticationEntityRepository).save(same(generatedAuth));

    assertThat(pair.getLeft(), is(equalTo(authToken)));
    assertThat(pair.getRight(), is(equalTo(refreshToken)));
  }

  @Test
  public void getOrGenerateToken_AuthenticationNotFound_BehavesAsExpected() {
    final var identityId = "theId";
    final var identity = new IdentityEntity();
    identity.setId(identityId);
    final var authId = "authId";
    final var auth = new AuthenticationEntity();
    auth.setId(authId);
    final var generatedAuth = new AuthenticationEntity();
    final var authToken = "auth token";
    final var refreshToken = "the refresh token";
    generatedAuth.setAuthToken(authToken);
    generatedAuth.setRefreshToken(refreshToken);

    when(mockAuthenticationEntityRepository.findFirstByIdentityIdOrderByCreatedDesc(
            same(identityId)))
        .thenReturn(Optional.empty());
    when(mockAuthenticationGenerator.generateAuthentication(same(identity)))
        .thenReturn(generatedAuth);

    final var pair = subject.getOrGenerateToken(identity);

    verify(mockAuthenticationEntityRepository)
        .findFirstByIdentityIdOrderByCreatedDesc(same(identityId));
    verify(mockAuthenticationGenerator).generateAuthentication(same(identity));
    verify(mockAuthenticationEntityRepository).save(same(generatedAuth));

    assertThat(pair.getLeft(), is(equalTo(authToken)));
    assertThat(pair.getRight(), is(equalTo(refreshToken)));
  }

  @Test
  public void getOrGenerateToken_AuthenticationFound_BehavesAsExpected() {
    final var identityId = "theId";
    final var identity = new IdentityEntity();
    identity.setId(identityId);
    final var authId = "authId";
    final var authToken = "auth token";
    final var refreshToken = "the refresh token";
    final var auth = new AuthenticationEntity();
    auth.setAuthToken(authToken);
    auth.setRefreshToken(refreshToken);
    auth.setId(authId);
    final var currentDate = Calendar.getInstance().getTime();
    final var thirtyMinBehind = DateUtils.addMinutes(currentDate, 30);
    auth.setExpiration(thirtyMinBehind);

    when(mockAuthenticationEntityRepository.findFirstByIdentityIdOrderByCreatedDesc(
            same(identityId)))
        .thenReturn(Optional.of(auth));

    final var pair = subject.getOrGenerateToken(identity);

    verify(mockAuthenticationEntityRepository)
        .findFirstByIdentityIdOrderByCreatedDesc(same(identityId));
    verify(mockAuthenticationGenerator, never()).generateAuthentication(any());
    verify(mockAuthenticationEntityRepository, never()).save(any());

    assertThat(pair.getLeft(), is(equalTo(authToken)));
    assertThat(pair.getRight(), is(equalTo(refreshToken)));
  }

  @Test
  public void logout_AllGood_BehavesAsExpected() {
    final var clientId = "the client id";
    final var tokenValue = "the tokenValue";
    final var identityId = "the identity id";
    final var tenantId = "the tenant id";
    final var accessToken = new AccessTokenEntity();
    accessToken.setIdentityId(identityId);
    final var identity = new IdentityEntity();
    identity.setParentTenantId(tenantId);
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);

    when(mockValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(tokenValue);
    when(mockAccessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(tokenValue), any(Date.class)))
        .thenReturn(Optional.of(accessToken));
    when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.of(identity));
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));

    subject.logout(clientId, mockServletRequest);

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockAccessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(tokenValue), any(Date.class));
    verify(mockIdentityRepository).findById(same(identityId));
    verify(mockApplicationRepository).findByClientId(same(clientId));
    verify(mockAccessTokenRepository).delete(accessToken);
  }

  @Test
  public void logout_AccessTokenNotFound_ReturnsNull() {
    final var clientId = "the client id";
    final var tokenValue = "the tokenValue";
    final var identityId = "the identity id";
    final var tenantId = "the tenant id";
    final var accessToken = new AccessTokenEntity();
    accessToken.setIdentityId(identityId);
    final var identity = new IdentityEntity();
    identity.setParentTenantId(tenantId);
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);

    when(mockValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(tokenValue);
    when(mockAccessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(tokenValue), any(Date.class)))
        .thenReturn(Optional.empty());

    subject.logout(clientId, mockServletRequest);

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockAccessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(tokenValue), any(Date.class));
  }

  @Test
  public void logout_IdentityNotFound_ExceptionThrown() {
    final var clientId = "the client id";
    final var tokenValue = "the tokenValue";
    final var identityId = "the identity id";
    final var tenantId = "the tenant id";
    final var accessToken = new AccessTokenEntity();
    accessToken.setIdentityId(identityId);
    final var identity = new IdentityEntity();
    identity.setParentTenantId(tenantId);
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);

    when(mockValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(tokenValue);
    when(mockAccessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(tokenValue), any(Date.class)))
        .thenReturn(Optional.of(accessToken));
    when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(
            NotAuthorizedException.class, () -> subject.logout(clientId, mockServletRequest));

    assertThat(exception.getMessage(), is(equalTo("Not Authorized")));

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockAccessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(tokenValue), any(Date.class));
    verify(mockIdentityRepository).findById(same(identityId));
  }

  @Test
  public void logout_ApplicationNotFound_ExceptionThrown() {
    final var clientId = "the client id";
    final var tokenValue = "the tokenValue";
    final var identityId = "the identity id";
    final var tenantId = "the tenant id";
    final var accessToken = new AccessTokenEntity();
    accessToken.setIdentityId(identityId);
    final var identity = new IdentityEntity();
    identity.setParentTenantId(tenantId);
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);

    when(mockValidator.isNotBlank(same(clientId))).thenReturn(true);
    when(mockTokenExtractor.extractBearerToken(same(mockServletRequest))).thenReturn(tokenValue);
    when(mockAccessTokenRepository.findFirstByAccessTokenAndExpirationAfter(
            same(tokenValue), any(Date.class)))
        .thenReturn(Optional.of(accessToken));
    when(mockIdentityRepository.findById(same(identityId))).thenReturn(Optional.of(identity));
    when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(
            NotAuthorizedException.class, () -> subject.logout(clientId, mockServletRequest));

    assertThat(exception.getMessage(), is(equalTo("Not Authorized")));

    verify(mockValidator).isNotBlank(same(clientId));
    verify(mockTokenExtractor).extractBearerToken(same(mockServletRequest));
    verify(mockAccessTokenRepository)
        .findFirstByAccessTokenAndExpirationAfter(same(tokenValue), any(Date.class));
    verify(mockIdentityRepository).findById(same(identityId));
    verify(mockApplicationRepository).findByClientId(same(clientId));
  }
}
