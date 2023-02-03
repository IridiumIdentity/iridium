/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import software.iridium.api.authentication.domain.InitiatePasswordResetRequest;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.entity.ApplicationEntity;
import software.iridium.api.entity.IdentityEmailEntity;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.PasswordResetTokenEntity;
import software.iridium.api.handler.PasswordEventHandler;
import software.iridium.api.instantiator.PasswordResetTokenEntityInstantiator;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.IdentityEmailEntityRepository;
import software.iridium.api.repository.IdentityEntityRepository;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {

  @Mock private IdentityEntityRepository mockIdentityRepository;
  @Mock private IdentityEmailEntityRepository emailRepository;
  @Mock private PasswordResetTokenEntityInstantiator mockTokenInstantiator;
  @Mock private PasswordEventHandler mockPasswordEventHandler;
  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @InjectMocks private PasswordService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockIdentityRepository,
        mockTokenInstantiator,
        mockPasswordEventHandler,
        mockApplicationRepository);
  }

  @Test
  public void initiatePasswordReset_AllGood_BehavesAsExpected() {
    final var username = "someone@somehwere.com";
    final var id = "theId";
    final var clientId = "theClientId";
    final var request = new InitiatePasswordResetRequest();
    request.setUsername(username);
    request.setClientId(clientId);
    final var identity = new IdentityEntity();
    final var emailEntity = new IdentityEmailEntity();
    emailEntity.setIdentity(identity);
    identity.setId(id);
    final var passwordResetToken = new PasswordResetTokenEntity();
    final var application = new ApplicationEntity();

    when(emailRepository.findByEmailAddress(same(username))).thenReturn(Optional.of(emailEntity));
    when(mockTokenInstantiator.instantiate(same(identity))).thenReturn(passwordResetToken);
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));

    assertThat(subject.initiatePasswordReset(request), is(true));

    verify(emailRepository).findByEmailAddress(same(username));
    verify(mockTokenInstantiator).instantiate(same(identity));
    verify(mockPasswordEventHandler)
        .handlePasswordResetInitiatedEvent(same(identity), same(clientId));
    verify(mockApplicationRepository).findByClientId(same(clientId));
  }

  @Test
  public void initiatePasswordReset_identityNotFound_ReturnsFalse() {
    final var username = "someone@somehwere.com";
    final var clientId = "theClientId";
    final var request = new InitiatePasswordResetRequest();
    request.setUsername(username);
    request.setClientId(clientId);
    final var application = new ApplicationEntity();

    when(emailRepository.findByEmailAddress(same(username))).thenReturn(Optional.empty());
    when(mockApplicationRepository.findByClientId(same(clientId)))
        .thenReturn(Optional.of(application));

    assertThat(subject.initiatePasswordReset(request), is(false));

    verify(emailRepository).findByEmailAddress(same(username));
    verify(mockApplicationRepository).findByClientId(same(clientId));
  }

  @Test
  public void initiatePasswordReset_ApplicationNotFound_ExceptionThrown() {
    final var username = "someone@somehwere.com";
    final var clientId = "theClientId";
    final var request = new InitiatePasswordResetRequest();
    request.setUsername(username);
    request.setClientId(clientId);

    when(mockApplicationRepository.findByClientId(same(clientId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(ResourceNotFoundException.class, () -> subject.initiatePasswordReset(request));

    verify(mockApplicationRepository).findByClientId(same(clientId));
    assertThat(
        exception.getMessage(), is(equalTo("application not found for clientId: " + clientId)));
  }
}
