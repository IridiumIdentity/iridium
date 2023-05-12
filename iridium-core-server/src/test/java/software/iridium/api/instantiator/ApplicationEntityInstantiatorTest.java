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
package software.iridium.api.instantiator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.entity.ApplicationTypeEntity;
import software.iridium.api.util.EncoderUtils;

@ExtendWith(MockitoExtension.class)
class ApplicationEntityInstantiatorTest {

  @Mock private EncoderUtils mockEncoderUtils;
  @InjectMocks private ApplicationEntityInstantiator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockEncoderUtils);
  }

  @Test
  public void instantiate_AllGood_InstantiatesAsExpected() throws NoSuchAlgorithmException {
    final var request = new ApplicationCreateRequest();
    final var applicationType = new ApplicationTypeEntity();
    final var orgId = "the org id";
    final var name = "the app name";
    final var description = "the description";
    final var homePageURL = "the home page URL";
    final var redirectURI = "the redirect URI";
    request.setName(name);
    request.setDescription(description);
    request.setHomepageURL(homePageURL);
    request.setCallbackURL(redirectURI);

    when(mockEncoderUtils.cryptoSecureToHex(
            same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH)))
        .thenCallRealMethod();

    final var response = subject.instantiate(request, applicationType, orgId);

    verify(mockEncoderUtils)
        .cryptoSecureToHex(same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH));

    MatcherAssert.assertThat(response.getApplicationType(), sameInstance(applicationType));
    MatcherAssert.assertThat(response.getTenantId(), is(equalTo(orgId)));
    MatcherAssert.assertThat(response.getName(), is(equalTo(name)));
    MatcherAssert.assertThat(
        response.getClientId().length(),
        is(equalTo(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH * 2)));
    assertThat(response.getDescription(), is(equalTo(description)));
    assertThat(response.getHomePageUrl(), is(equalTo(homePageURL)));
    assertThat(response.getRedirectUri(), is(equalTo(redirectURI)));
  }

  @Test
  public void instantiate_ErrorEncoding_ExceptionThrown() throws NoSuchAlgorithmException {
    final var request = new ApplicationCreateRequest();
    final var appTypeId = "the app type id";
    final var applicationType = new ApplicationTypeEntity();
    applicationType.setId(appTypeId);
    final var orgId = "the org id";
    final var name = "the app name";
    request.setName(name);

    when(mockEncoderUtils.cryptoSecureToHex(
            same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH)))
        .thenThrow(new NoSuchAlgorithmException());

    final var exception =
        assertThrows(
            RuntimeException.class, () -> subject.instantiate(request, applicationType, orgId));

    verify(mockEncoderUtils)
        .cryptoSecureToHex(same(ApplicationEntityInstantiator.CLIENT_ID_SEED_LENGTH));
    assertThat(
        exception.getMessage(),
        is(equalTo("error creating client id for application: " + appTypeId)));
  }
}
