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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

@ExtendWith(MockitoExtension.class)
class AuthorizationGrantTypeParamAttributeValidatorTest {

  @Mock private AttributeValidator mockAttributeValidator;
  @InjectMocks private AuthorizationGrantTypeParamValidator subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockAttributeValidator);
  }

  @Test
  public void validate_AllGood_BehavesAsExpected() {
    final var clientId = "the client id";
    final var params = new HashMap<String, String>();
    params.put(
        AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(),
        AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue());
    params.put(AuthorizationCodeFlowConstants.CLIENT_ID.getValue(), clientId);

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    subject.validate(params);

    verify(mockAttributeValidator)
        .isNotBlank(eq(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()));
    verify(mockAttributeValidator).isNotBlank(same(clientId));
  }

  @Test
  public void validate_responseTypeIsBlank_ExceptionThrown() {
    final var params = new HashMap<String, String>();

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(IllegalArgumentException.class, () -> subject.validate(params));

    assertThat(exception.getMessage(), is(equalTo("response_type must not be blank")));

    verify(mockAttributeValidator).isNotBlank(eq(""));
  }

  @Test
  public void validate_ClientIdIsBlank_ExceptionThrown() {
    final var params = new HashMap<String, String>();
    params.put(
        AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue(),
        AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue());

    when(mockAttributeValidator.isNotBlank(anyString())).thenCallRealMethod();

    final var exception =
        assertThrows(IllegalArgumentException.class, () -> subject.validate(params));

    assertThat(exception.getMessage(), is(equalTo("client_id must not be blank")));

    verify(mockAttributeValidator)
        .isNotBlank(eq(AuthorizationCodeFlowConstants.AUTHORIZATION_CODE.getValue()));
    verify(mockAttributeValidator).isNotBlank(eq(""));
  }
}
