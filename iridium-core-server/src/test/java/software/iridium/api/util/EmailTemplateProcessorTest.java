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
package software.iridium.api.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import software.iridium.api.email.domain.EmailSendRequest;

@ExtendWith(MockitoExtension.class)
class EmailTemplateProcessorTest {

  @Mock private FreeMarkerConfigurer mockConfigurer;
  @Mock private Template mockTemplate;
  @Mock private Configuration mockConfiguration;
  @InjectMocks private EmailTemplateProcessor subject;

  @AfterEach
  public void ensureNoUnexpectedMockInteractions() {
    Mockito.verifyNoMoreInteractions(mockConfigurer, mockTemplate, mockConfiguration);
  }

  @Test
  public void processTemplateIntoString_AllGood_BehavesAsExpected()
      throws TemplateException, IOException {
    final var request = new EmailSendRequest();
    final var props = new HashMap<String, Object>();
    request.setProperties(props);
    final var templateName = "theName";
    final var templateAsString = "the template as a string";
    request.setTemplate(templateName);
    when(mockConfigurer.getConfiguration()).thenReturn(mockConfiguration);
    when(mockConfiguration.getTemplate(anyString())).thenReturn(mockTemplate);
    try (MockedStatic<FreeMarkerTemplateUtils> utilities =
        Mockito.mockStatic(FreeMarkerTemplateUtils.class)) {
      utilities
          .when(() -> FreeMarkerTemplateUtils.processTemplateIntoString(mockTemplate, props))
          .thenReturn(templateAsString);

      final var response = subject.processTemplateIntoString(request);

      assertThat(response, is(equalTo(templateAsString)));

      verify(mockConfigurer).getConfiguration();
      verify(mockConfiguration).getTemplate(eq("theName.ftl"));
    }
  }
}
