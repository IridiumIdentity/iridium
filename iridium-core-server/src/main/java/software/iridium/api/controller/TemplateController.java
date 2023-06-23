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

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.service.TemplateService;

@Controller
public class TemplateController {

  @Autowired private TemplateService templateService;

  private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);

  @GetMapping("/login")
  public String retrieveLoginForm(
      final AuthenticationRequest request,
      final Model model,
      final @RequestParam Map<String, String> params,
      final HttpServletRequest servletRequest) {
    logger.info("loading login for: {}", servletRequest.getRequestURL().toString());
    logger.info(
        "loading login for subdomain: {}",
        servletRequest.getRequestURL().toString().split("\\.")[0]);
    return templateService.describeIndex(model, servletRequest, params);
  }
}
