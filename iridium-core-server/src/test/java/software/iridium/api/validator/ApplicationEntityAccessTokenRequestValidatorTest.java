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
package software.iridium.api.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.util.AttributeValidator;
import software.iridium.api.util.AuthorizationCodeFlowConstants;
import software.iridium.entity.ApplicationEntity;

@ExtendWith(MockitoExtension.class)
class ApplicationEntityAccessTokenRequestValidatorTest {

  @Mock private AttributeValidator mockValidator;
  @InjectMocks private ApplicationEntityAccessTokenRequestValidator subject;

  @AfterEach
  public void setUpForEachTestCase() {
    Mockito.verifyNoMoreInteractions(mockValidator);
  }

  @Test
  public void validate_AllGood_BehavesAsExpected() {
    final var redirectUri = "the redirect uri";
    final var application = new ApplicationEntity();
    application.setRedirectUri(redirectUri);

    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), redirectUri);

    when(mockValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();

    subject.validate(application, params);

    verify(mockValidator).isNotBlank(same(redirectUri));
    verify(mockValidator).doesNotEqual(eq(redirectUri), eq(redirectUri));
  }

  @Test
  public void validate_RedirectUriDoesNotMatch_ExceptionThrown() {
    final var redirectUri = "the redirect uri";
    final var theOtherRedirectUri = "the other uri";
    final var application = new ApplicationEntity();
    application.setRedirectUri(theOtherRedirectUri);

    final var params = new HashMap<String, String>();
    params.put(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue(), redirectUri);

    when(mockValidator.isNotBlank(anyString())).thenCallRealMethod();
    when(mockValidator.doesNotEqual(anyString(), anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(IllegalArgumentException.class, () -> subject.validate(application, params));

    assertThat(exception.getMessage(), is(equalTo("redirect_uri is not valid")));

    verify(mockValidator).isNotBlank(same(redirectUri));
    verify(mockValidator).doesNotEqual(eq(theOtherRedirectUri), eq(redirectUri));
  }
}
