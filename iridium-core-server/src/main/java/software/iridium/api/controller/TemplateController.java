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
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.InitiatePasswordResetRequest;
import software.iridium.api.authentication.domain.PasswordResetRequest;
import software.iridium.api.service.TemplateService;
import software.iridium.api.util.ServletTokenExtractor;

@Controller
public class TemplateController {

  @Resource private TemplateService templateService;

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

  @GetMapping("/reset-password")
  public String retrieveRestPasswordForm(
      final PasswordResetRequest passwordResetRequest,
      final Model model,
      final HttpServletRequest servletRequest) {
    return "reset-password";
  }

  @GetMapping("/initiate-reset-password")
  public String retrieveRestPasswordForm(
      final InitiatePasswordResetRequest initiatePasswordResetRequest,
      final Model model,
      final HttpServletRequest servletRequest) {
    return "initiate-reset-password";
  }

  @GetMapping("/confirmation")
  public String confirmRegistration(final Model model) {
    return "confirmation";
  }

  @GetMapping("/register")
  public String register(
      final CreateIdentityRequest createIdentityRequest,
      final Model model,
      final HttpServletRequest servletRequest) {
    logger.info("loading login for: {}", servletRequest.getRequestURL().toString());
    logger.info(
        "loading login for subdomain: {}",
        servletRequest.getRequestURL().toString().split("\\.")[0]);
    return templateService.describeRegister(model, servletRequest);
  }

  @GetMapping(value = "/authorize")
  public String retrieveAuthorizationPage(
      ModelMap model, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    logger.info("request headers: {}", servletRequest.getHeaderNames().toString());
    logger.info("response headers: {}", servletResponse.getHeaderNames());
    Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(servletRequest);
    if (inputFlashMap != null) {
      String token = (String) inputFlashMap.get(ServletTokenExtractor.IRIDIUM_TOKEN_HEADER_VALUE);
      model.addAttribute("userToken", token);
    }
    return "authorize";
  }
}
