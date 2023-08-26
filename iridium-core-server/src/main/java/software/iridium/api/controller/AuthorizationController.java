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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import software.iridium.api.authentication.domain.AccessTokenResponse;
import software.iridium.api.authentication.domain.ApplicationAuthorizationFormRequest;
import software.iridium.api.service.AuthorizationService;

@CrossOrigin
@RestController
public class AuthorizationController {

  private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

  @Autowired private AuthorizationService authorizationService;

  @PostMapping(value = "/oauth/external/authorize")
  public RedirectView proxyAuthorizationRequestToProvider(
      ModelMap model,
      RedirectAttributes redirectAttributes,
      @RequestParam(value = "response_type", required = false) final String responseType,
      @RequestParam(value = "state", required = false) final String state,
      @RequestParam(value = "redirect_uri", required = false) final String redirectUri,
      @RequestParam(value = "client_id", required = false) final String clientId,
      @RequestParam(value = "provider", required = false) final String provider) {

    final var redirectDestination =
        authorizationService.proxyAuthorizationRequestToProvider(
            responseType, state, redirectUri, clientId, provider);
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(redirectDestination);
    return redirectView;
  }

  @RequestMapping(
      value = "/oauth/authorize",
      method = {RequestMethod.POST, RequestMethod.GET})
  public RedirectView authorize(
      HttpServletRequest servletRequest,
      final ApplicationAuthorizationFormRequest formRequest,
      @RequestParam Map<String, String> params) {
    logger.info("initiating authorization");
    final var redirectUri = authorizationService.authorize(formRequest, params, servletRequest);
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl(redirectUri);
    return redirectView;
  }

  @PostMapping(value = "/oauth/token")
  public AccessTokenResponse exchange(
      HttpServletRequest servletRequest, @RequestParam Map<String, String> params) {
    logger.info("finalizing authorization");
    return authorizationService.exchange(servletRequest, params);
  }
}
