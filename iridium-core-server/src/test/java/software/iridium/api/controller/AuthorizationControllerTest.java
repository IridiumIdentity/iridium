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

package software.iridium.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import software.iridium.api.authentication.domain.ApplicationAuthorizationFormRequest;
import software.iridium.api.service.AuthorizationService;

@ExtendWith(MockitoExtension.class)
class AuthorizationControllerTest {

  @Mock private AuthorizationService mockAuthorizationService;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private ModelMap mockModelMap;
  @Mock private RedirectAttributes mockAttributes;
  @InjectMocks private AuthorizationController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    verifyNoMoreInteractions(
        mockAuthorizationService, mockServletRequest, mockModelMap, mockAttributes);
  }

  @Test
  public void proxyAuthorizationRequestToProvider_AllGood_BehavesAsExpected() {
    final var responseType = "responseType";
    final var state = "state";
    final var redirectUri = "redirectUri";
    final var clientId = "the client id";
    final var provider = "the provider";
    final var destination = "the redirect destination";

    when(mockAuthorizationService.proxyAuthorizationRequestToProvider(
            same(responseType), same(state), same(redirectUri), same(clientId), same(provider)))
        .thenReturn(destination);

    final var response =
        subject.proxyAuthorizationRequestToProvider(
            mockModelMap, mockAttributes, responseType, state, redirectUri, clientId, provider);

    verify(mockAuthorizationService)
        .proxyAuthorizationRequestToProvider(
            same(responseType), same(state), same(redirectUri), same(clientId), same(provider));

    assertThat(response.getUrl(), is(equalTo(destination)));
  }

  @Test
  public void authorize_AllGood_BehavesAsExpected() {
    final var params = new HashMap<String, String>();
    final var redirectUri = "htp://localhost/redirect";
    final var formRequest = new ApplicationAuthorizationFormRequest();

    when(mockAuthorizationService.authorize(
            same(formRequest), same(params), same(mockServletRequest)))
        .thenReturn(redirectUri);

    final var response = subject.authorize(mockServletRequest, formRequest, params);

    verify(mockAuthorizationService)
        .authorize(same(formRequest), same(params), same(mockServletRequest));

    assertThat(response.getUrl(), is(equalTo(redirectUri)));
  }

  @Test
  public void exchange_AllGood_BehavesAsExpected() {
    final var params = new HashMap<String, String>();

    subject.exchange(mockServletRequest, params);

    verify(mockAuthorizationService).exchange(same(mockServletRequest), same(params));
  }
}
