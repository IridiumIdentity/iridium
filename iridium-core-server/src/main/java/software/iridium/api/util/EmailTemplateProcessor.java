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

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import software.iridium.api.email.domain.EmailSendRequest;

@Component
public class EmailTemplateProcessor {
  @Autowired private FreeMarkerConfigurer freemarkerConfigurer;

  public String processTemplateIntoString(final EmailSendRequest request)
      throws IOException, TemplateException {
    Template freemarkerTemplate =
        freemarkerConfigurer.getConfiguration().getTemplate(request.getTemplate() + ".ftl");
    return processTemplateIntoString(request, freemarkerTemplate);
  }

  private String processTemplateIntoString(
      final EmailSendRequest request, final Template freemarkerTemplate)
      throws IOException, TemplateException {
    return FreeMarkerTemplateUtils.processTemplateIntoString(
        freemarkerTemplate, request.getProperties());
  }
}
