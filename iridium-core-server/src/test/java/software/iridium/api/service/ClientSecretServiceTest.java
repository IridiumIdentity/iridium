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
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.ClientSecretCreateResponse;
import software.iridium.api.base.error.ClientCallException;
import software.iridium.api.base.error.ResourceNotFoundException;
import software.iridium.api.instantiator.ClientSecretEntityInstantiator;
import software.iridium.api.mapper.ClientSecretCreateResponseMapper;
import software.iridium.api.repository.ApplicationEntityRepository;
import software.iridium.api.repository.ClientSecretEntityRepository;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.EncoderUtils;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ApplicationTypeEntity;
import software.iridium.entity.ClientSecretEntity;

@ExtendWith(MockitoExtension.class)
class ClientSecretServiceTest {

  @Mock private AttributeValidator mockAttributeValidator;
  @Mock private ApplicationEntityRepository mockApplicationRepository;
  @Mock private ClientSecretEntityInstantiator mockClientSecretInstantiator;
  @Mock private ClientSecretCreateResponseMapper mockResponseMapper;
  @Mock private ClientSecretEntityRepository mockClientSecretRepository;
  @Mock private EncoderUtils mockEncoderUtils;
  @InjectMocks private ClientSecretService subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockAttributeValidator,
        mockApplicationRepository,
        mockClientSecretInstantiator,
        mockResponseMapper,
        mockClientSecretInstantiator,
        mockEncoderUtils);
  }

  @Test
  public void create_AllGood_BehavesAsExpected() throws NoSuchAlgorithmException {
    final var applicationId = "the app id";
    final var application = new ApplicationEntity();
    final var type = new ApplicationTypeEntity();
    type.setRequiresSecret(true);
    application.setApplicationType(type);
    final var clientSecret = new ClientSecretEntity();
    final var response = new ClientSecretCreateResponse();

    when(mockAttributeValidator.isUuid(same(applicationId))).thenReturn(true);
    when(mockApplicationRepository.findById(same(applicationId)))
        .thenReturn(Optional.of(application));
    when(mockClientSecretInstantiator.instantiate(same(application), anyString()))
        .thenReturn(clientSecret);
    when(mockResponseMapper.map(same(clientSecret), anyString())).thenReturn(response);
    when(mockEncoderUtils.cryptoSecureToHex(same(ClientSecretService.SEED_LENGTH)))
        .thenCallRealMethod();

    subject.create(applicationId);

    verify(mockAttributeValidator).isUuid(same(applicationId));
    verify(mockApplicationRepository).findById(same(applicationId));
    verify(mockClientSecretInstantiator).instantiate(same(application), anyString());
    verify(mockClientSecretRepository, never()).save(same(clientSecret));
    verify(mockResponseMapper).map(same(clientSecret), anyString());
    verify(mockEncoderUtils).cryptoSecureToHex(same(ClientSecretService.SEED_LENGTH));
  }

  @Test
  public void create_ApplicationNotFoundForId_ExceptionThrown() {
    final var applicationId = "the app id";

    when(mockAttributeValidator.isUuid(same(applicationId))).thenReturn(true);
    when(mockApplicationRepository.findById(same(applicationId))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(ResourceNotFoundException.class, () -> subject.create(applicationId));

    verify(mockAttributeValidator).isUuid(same(applicationId));
    verify(mockApplicationRepository).findById(same(applicationId));

    assertThat(
        exception.getMessage(), is(equalTo("application not found for id: " + applicationId)));
  }

  @Test
  public void create_ApplicationDoesNotRequireSecret_ExceptionThrown() {
    final var applicationId = "the app id";
    final var application = new ApplicationEntity();
    application.setId(applicationId);
    final var type = new ApplicationTypeEntity();
    type.setRequiresSecret(false);
    application.setApplicationType(type);

    when(mockAttributeValidator.isUuid(same(applicationId))).thenReturn(true);
    when(mockApplicationRepository.findById(same(applicationId)))
        .thenReturn(Optional.of(application));

    final var exception =
        assertThrows(ClientCallException.class, () -> subject.create(applicationId));

    verify(mockAttributeValidator).isUuid(same(applicationId));
    verify(mockApplicationRepository).findById(same(applicationId));

    assertThat(
        exception.getMessage(),
        is(
            equalTo(
                "incorrect attempt to create secret for non secret application: "
                    + applicationId)));
  }

  @Test
  public void create_EncoderError_ExceptionThrown() throws NoSuchAlgorithmException {
    final var applicationId = "the app id";
    final var application = new ApplicationEntity();
    final var type = new ApplicationTypeEntity();
    type.setRequiresSecret(true);
    application.setApplicationType(type);

    when(mockAttributeValidator.isUuid(same(applicationId))).thenReturn(true);
    when(mockApplicationRepository.findById(same(applicationId)))
        .thenReturn(Optional.of(application));
    when(mockEncoderUtils.cryptoSecureToHex(same(ClientSecretService.SEED_LENGTH)))
        .thenThrow(NoSuchAlgorithmException.class);

    final var exception = assertThrows(RuntimeException.class, () -> subject.create(applicationId));

    verify(mockAttributeValidator).isUuid(same(applicationId));
    verify(mockApplicationRepository).findById(same(applicationId));
    verify(mockEncoderUtils).cryptoSecureToHex(same(ClientSecretService.SEED_LENGTH));

    assertThat(exception.getMessage(), is(equalTo("error creating secret for application")));
  }

  @Test
  public void delete_AllGood_BehavesAsExpected() {
    final var applicationId = "the app id";
    final var clientSecretId = "the client id";
    final var clientSecret = new ClientSecretEntity();
    final var application = new ApplicationEntity();
    application.setId(applicationId);
    clientSecret.setApplication(application);

    when(mockAttributeValidator.isUuid(same(applicationId))).thenReturn(true);
    when(mockAttributeValidator.isUuid(same(clientSecretId))).thenReturn(true);
    when(mockClientSecretRepository.findById(same(clientSecretId)))
        .thenReturn(Optional.of(clientSecret));

    subject.delete(applicationId, clientSecretId);

    verify(mockAttributeValidator).isUuid(same(applicationId));
    verify(mockAttributeValidator).isUuid(same(clientSecretId));
    verify(mockClientSecretRepository).findById(same(clientSecretId));
    verify(mockClientSecretRepository).delete(same(clientSecret));
  }

  @Test
  public void delete_SecretNotAssociatedWithApplication_ExceptionThrown() {
    final var applicationId = "the app id";
    final var clientSecretId = "the client id";
    final var otherApplicationId = "the other app id";
    final var clientSecret = new ClientSecretEntity();
    final var application = new ApplicationEntity();
    application.setId(otherApplicationId);
    clientSecret.setApplication(application);

    when(mockAttributeValidator.isUuid(same(applicationId))).thenReturn(true);
    when(mockAttributeValidator.isUuid(same(clientSecretId))).thenReturn(true);
    when(mockClientSecretRepository.findById(same(clientSecretId)))
        .thenReturn(Optional.of(clientSecret));

    final var exception =
        assertThrows(
            IllegalArgumentException.class, () -> subject.delete(applicationId, clientSecretId));

    verify(mockAttributeValidator).isUuid(same(applicationId));
    verify(mockAttributeValidator).isUuid(same(clientSecretId));
    verify(mockClientSecretRepository).findById(same(clientSecretId));
    assertThat(
        exception.getMessage(),
        is(
            equalTo(
                String.format(
                    "client secret %s not associated with application %s",
                    clientSecretId, applicationId))));
  }
}
