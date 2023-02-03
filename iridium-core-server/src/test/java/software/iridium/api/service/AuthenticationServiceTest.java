/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.base.error.NotAuthorizedException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.TenantEntity;
import software.iridium.api.repository.*;
import software.iridium.api.validator.AuthenticationRequestParamValidator;
import software.iridium.api.validator.AuthenticationRequestValidator;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock private IdentityEntityRepository mockIdentityRepository;
  @Mock private AuthenticationEntityRepository mockAuthenticationRepository;
  @Mock private BCryptPasswordEncoder mockEncoder;
  @Mock private TokenManager mockTokenManager;
  @Mock private IdentityEmailEntityRepository mockEmailRepository;
  @Mock private AuthenticationRequestValidator mockRequestValidator;
  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @Mock private AuthenticationRequestParamValidator mockAuthRequestParamValidator;
  @Mock private TenantEntityRepository mockTenantRepository;
  @InjectMocks private AuthenticationService subject;

  @AfterEach
  public void verifyNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockIdentityRepository,
        mockAuthenticationRepository,
        mockEncoder,
        mockTokenManager,
        mockRequestValidator,
        mockEmailRepository,
        mockApplicationRepository,
        mockAuthRequestParamValidator,
        mockTenantRepository);
  }

  @Test
  public void authenticate_AllGood_BehavesAsExpected() {
    final var username = "theUsername";
    final var clearTextPassword = "the password";
    final var encodedPassword = "the pa$$word";
    final var request = new AuthenticationRequest();
    request.setPassword(clearTextPassword);
    request.setUsername(username);
    final var identity = new IdentityEntity();
    identity.setLocked(false);
    identity.setEncodedPassword(encodedPassword);
    identity.setRequiresPasswordChange(false);
    final var email = new IdentityEmailEntity();
    email.setIdentity(identity);
    final var emailOpt = Optional.of(email);

    final var userToken = "theUserToken";
    final var refreshToken = "theRefreshToken";
    final var tenantId = "the tenant id";
    final var tokens = new ImmutablePair<String, String>(userToken, refreshToken);
    final var applicationClientId = "the id";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    request.setClientId(applicationClientId);
    final var params = new HashMap<String, String>();
    final var tenant = new TenantEntity();

    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(username), same(tenantId)))
        .thenReturn(emailOpt);
    when(mockEncoder.matches(same(clearTextPassword), same(encodedPassword))).thenReturn(true);
    when(mockTokenManager.getOrGenerateToken(same(identity))).thenReturn(tokens);
    when(mockApplicationRepository.findByClientId(same(applicationClientId)))
        .thenReturn(Optional.of(application));
    when(mockTenantRepository.findById(tenantId)).thenReturn(Optional.of(tenant));

    final var response = subject.authenticate(request, params);

    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(username), same(tenantId));
    verify(mockEncoder).matches(same(clearTextPassword), same(encodedPassword));
    verify(mockTokenManager).getOrGenerateToken(same(identity));
    verify(mockIdentityRepository).save(same(identity));
    verify(mockRequestValidator).validate(request);
    verify(mockAuthRequestParamValidator).validate(same(params));
    verify(mockApplicationRepository).findByClientId(same(applicationClientId));
    verify(mockTenantRepository).findById(tenantId);

    MatcherAssert.assertThat(response.getUserToken(), is(equalTo(userToken)));
    MatcherAssert.assertThat(response.getUserRefreshToken(), is(equalTo(refreshToken)));
    MatcherAssert.assertThat(response.getPasswordResetLink(), nullValue());
  }

  @Test
  public void authenticate_IdentityIsLocked_ExceptionThrown() {
    final var username = "theUsername";
    final var request = new AuthenticationRequest();
    request.setUsername(username);
    final var identity = new IdentityEntity();
    identity.setLocked(true);
    final var email = new IdentityEmailEntity();
    email.setIdentity(identity);
    final var emailOpt = Optional.of(email);
    final var tenantId = "the tenant id";
    final var applicationClientId = "the id";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    request.setClientId(applicationClientId);
    final var params = new HashMap<String, String>();

    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(username), same(tenantId)))
        .thenReturn(emailOpt);
    when(mockApplicationRepository.findByClientId(same(applicationClientId)))
        .thenReturn(Optional.of(application));

    final var exception =
        assertThrows(NotAuthorizedException.class, () -> subject.authenticate(request, params));

    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(username), same(tenantId));
    verify(mockEncoder, never()).matches(anyString(), anyString());
    verify(mockTokenManager, never()).getOrGenerateToken(same(identity));
    verify(mockIdentityRepository).save(same(identity));
    verify(mockRequestValidator).validate(request);
    verify(mockApplicationRepository).findByClientId(same(applicationClientId));
    verify(mockAuthRequestParamValidator).validate(same(params));

    assertThat(exception.getMessage(), is(equalTo("NOT AUTHORIZED")));
  }

  @Test
  public void authenticate_RequiresPasswordChange_ReturnsPasswordResetUrl() {
    final var username = "theUsername";
    final var encodedPassword = "theEncodedP4$$word";
    final var rawPassword = "the raw password";
    final var request = new AuthenticationRequest();
    request.setUsername(username);
    request.setPassword(rawPassword);
    final var identity = new IdentityEntity();
    identity.setLocked(false);
    identity.setRequiresPasswordChange(true);
    identity.setEncodedPassword(encodedPassword);
    final var email = new IdentityEmailEntity();
    email.setIdentity(identity);
    final var emailOpt = Optional.of(email);
    final var userToken = "theUserToken";
    final var refreshToken = "theRefreshToken";
    final var tokens = new ImmutablePair<String, String>(userToken, refreshToken);
    final var tenantId = "the tenant id";
    final var applicationClientId = "the id";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    request.setClientId(applicationClientId);
    final var params = new HashMap<String, String>();

    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(username), same(tenantId)))
        .thenReturn(emailOpt);
    when(mockEncoder.matches(same(rawPassword), same(encodedPassword))).thenReturn(true);
    when(mockTokenManager.getOrGenerateToken(same(identity))).thenReturn(tokens);
    when(mockApplicationRepository.findByClientId(same(applicationClientId)))
        .thenReturn(Optional.of(application));

    final var exception =
        assertThrows(NotAuthorizedException.class, () -> subject.authenticate(request, params));

    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(username), same(tenantId));
    verify(mockEncoder).matches(same(rawPassword), same(encodedPassword));
    verify(mockTokenManager).getOrGenerateToken(same(identity));
    verify(mockRequestValidator).validate(request);
    verify(mockAuthRequestParamValidator).validate(same(params));
    verify(mockApplicationRepository).findByClientId(same(applicationClientId));
    verify(mockIdentityRepository).save(same(identity));

    assertThat(exception.getMessage(), is(equalTo("NOT AUTHORIZED")));
    assertThat(exception.getCode(), is(equalTo("401")));
  }

  @Test
  public void authenticate_PasswordsDoNotMatch_ExceptionThrown() {
    final var username = "theUsername";
    final var clearTextPassword = "the password";
    final var encodedPassword = "the pa$$word";
    final var request = new AuthenticationRequest();
    request.setPassword(clearTextPassword);
    request.setUsername(username);
    final var identity = new IdentityEntity();
    identity.setLocked(false);
    identity.setEncodedPassword(encodedPassword);

    final var email = new IdentityEmailEntity();
    email.setIdentity(identity);
    final var emailOpt = Optional.of(email);
    final var tenantId = "the tenant id";
    final var applicationClientId = "the id";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    request.setClientId(applicationClientId);
    final var params = new HashMap<String, String>();

    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(username), same(tenantId)))
        .thenReturn(emailOpt);
    when(mockEncoder.matches(same(clearTextPassword), same(encodedPassword))).thenReturn(false);
    when(mockApplicationRepository.findByClientId(same(applicationClientId)))
        .thenReturn(Optional.of(application));

    final var exception =
        assertThrows(NotAuthorizedException.class, () -> subject.authenticate(request, params));

    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(username), same(tenantId));
    verify(mockEncoder).matches(same(clearTextPassword), same(encodedPassword));
    verify(mockTokenManager, never()).getOrGenerateToken(same(identity));
    verify(mockIdentityRepository).save(same(identity));
    verify(mockRequestValidator).validate(request);
    verify(mockAuthRequestParamValidator).validate(same(params));
    verify(mockApplicationRepository).findByClientId(same(applicationClientId));

    assertThat(exception.getMessage(), is(equalTo("NOT AUTHORIZED")));
    assertThat(exception.getCode(), is(equalTo("401")));
  }

  @Test
  public void authenticate_emailNotFoundWithApplicationId_ExceptionThrown() {
    final var username = "theUsername";
    final var request = new AuthenticationRequest();
    request.setUsername(username);
    final var tenantId = "the tenant id";
    final var applicationClientId = "the id";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    request.setClientId(applicationClientId);
    final var params = new HashMap<String, String>();

    when(mockEmailRepository.findByEmailAddressAndIdentity_ParentTenantId(
            same(username), same(tenantId)))
        .thenReturn(Optional.empty());
    when(mockApplicationRepository.findByClientId(same(applicationClientId)))
        .thenReturn(Optional.of(application));

    final var exception =
        assertThrows(NotAuthorizedException.class, () -> subject.authenticate(request, params));

    verify(mockRequestValidator).validate(request);
    verify(mockAuthRequestParamValidator).validate(same(params));
    verify(mockEmailRepository)
        .findByEmailAddressAndIdentity_ParentTenantId(same(username), same(tenantId));
    verify(mockApplicationRepository).findByClientId(same(applicationClientId));

    assertThat(exception.getMessage(), is(equalTo("NOT AUTHORIZED")));
    assertThat(exception.getCode(), is(equalTo("401")));
  }

  @Test
  public void authenticate_ApplicationNotFoundWithClientId_ExceptionThrown() {
    final var username = "theUsername";
    final var request = new AuthenticationRequest();
    request.setUsername(username);
    final var tenantId = "the tenant id";
    final var applicationClientId = "the id";
    final var application = new ApplicationEntity();
    application.setTenantId(tenantId);
    request.setClientId(applicationClientId);
    final var params = new HashMap<String, String>();

    when(mockApplicationRepository.findByClientId(same(applicationClientId)))
        .thenReturn(Optional.empty());

    final var exception =
        assertThrows(ResourceNotFoundException.class, () -> subject.authenticate(request, params));

    verify(mockRequestValidator).validate(request);
    verify(mockAuthRequestParamValidator).validate(same(params));
    verify(mockApplicationRepository).findByClientId(same(applicationClientId));
    assertThat(
        exception.getMessage(),
        is(equalTo("application not found for client id: " + applicationClientId)));
  }
}
