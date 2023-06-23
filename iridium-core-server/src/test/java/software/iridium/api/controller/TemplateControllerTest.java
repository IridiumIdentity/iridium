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
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.service.TemplateService;
import software.iridium.api.util.ServletTokenExtractor;

@ExtendWith(MockitoExtension.class)
class TemplateControllerTest {

  @Mock private TemplateService mockTemplateService;
  @Mock private Model mockModel;
  @Mock private ModelMap mockModelMap;
  @Mock private HttpServletRequest mockServletRequest;
  @Mock private HttpServletResponse mockResponse;
  @InjectMocks private TemplateController subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(
        mockTemplateService, mockModel, mockServletRequest, mockModelMap, mockResponse);
  }

  @Test
  public void retrieveLoginForm_AllGood_BehavesAsExpected() {
    final var authRequest = new AuthenticationRequest();
    final var params = new HashMap<String, String>();

    when(mockServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://someurl.com"));

    subject.retrieveLoginForm(authRequest, mockModel, params, mockServletRequest);

    verify(mockServletRequest, times(2)).getRequestURL();
    verify(mockTemplateService)
        .describeIndex(same(mockModel), same(mockServletRequest), same(params));
  }

  @Test
  public void confirmRegistration_AllGood_BehavesAsExpected() {

    assertThat(subject.confirmRegistration(mockModel), is(equalTo("confirmation")));
  }

  @Test
  public void register_AllGood_BehavesAsExpected() {
    final var request = new CreateIdentityRequest();

    when(mockServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://someurl.com"));

    subject.register(request, mockModel, mockServletRequest);

    verify(mockServletRequest, times(2)).getRequestURL();
    verify(mockTemplateService).describeRegister(same(mockModel), same(mockServletRequest));
  }

  @Test
  public void retrieveAuthorizationPage_AllGood_BehavesAsExpected() {
    final var userToken = "the user token";
    final var inputFlashMap = new HashMap<String, String>();
    inputFlashMap.put(ServletTokenExtractor.IRIDIUM_TOKEN_HEADER_VALUE, userToken);
    try (MockedStatic<RequestContextUtils> mockContext =
        Mockito.mockStatic(RequestContextUtils.class)) {
      mockContext
          .when(() -> RequestContextUtils.getInputFlashMap(mockServletRequest))
          .thenReturn(inputFlashMap);

      assertThat(
          subject.retrieveAuthorizationPage(mockModelMap, mockServletRequest, mockResponse),
          is(equalTo("authorize")));

      verify(mockModelMap).addAttribute(eq("userToken"), same(userToken));
    }
  }
}
