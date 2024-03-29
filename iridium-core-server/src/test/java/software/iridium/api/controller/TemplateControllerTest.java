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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import software.iridium.api.service.TemplateService;

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
    final var params = new HashMap<String, String>();

    when(mockServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://someurl.com"));

    subject.retrieveLoginForm(mockModel, params, mockServletRequest);

    verify(mockServletRequest, times(2)).getRequestURL();
    verify(mockTemplateService)
        .describeIndex(same(mockModel), same(mockServletRequest), same(params));
  }
}
