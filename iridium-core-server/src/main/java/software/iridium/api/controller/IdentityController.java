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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import software.iridium.api.authentication.domain.CreatePasskeyRequest;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.authentication.domain.IdentitySummaryResponse;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.base.domain.PagedListResponse;
import software.iridium.api.service.IdentityService;

@CrossOrigin
@RestController
public class IdentityController {

  private static final Logger logger = LoggerFactory.getLogger(IdentityController.class);

  @Autowired private IdentityService identityService;

  @RequestMapping(
      value = "/identities",
      method = RequestMethod.GET,
      produces = IdentityResponse.MEDIA_TYPE)
  public ApiDataResponse<IdentityResponse> getIdentity(HttpServletRequest request) {
    return new ApiDataResponse<>(identityService.getIdentity(request));
  }

  @GetMapping(
      value = "/tenants/{tenant-id}/identities",
      produces = IdentitySummaryResponse.MEDIA_TYPE_LIST)
  public PagedListResponse<IdentitySummaryResponse> getPage(
      final HttpServletRequest servletRequest,
      @PathVariable("tenant-id") final String tenantId,
      @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "20") final Integer size,
      @RequestParam(value = "active", defaultValue = "true") final Boolean active) {
    return identityService.getPage(servletRequest, tenantId, page, size, active);
  }

  @PostMapping(value = "/identities")
  public RedirectView createWithFormSubmit(
      @ModelAttribute("createPasskeyRequest") final CreatePasskeyRequest request,
      @RequestParam(value = "response_type", required = false) final String responseType,
      @RequestParam(value = "state", required = false) final String state,
      @RequestParam(value = "redirect_uri", required = false) final String redirectUri,
      @RequestParam(value = "client_id", required = false) final String clientId,
      @RequestParam(value = "code_challenge_method", required = false)
          final String codeChallengeMethod,
      @RequestParam(value = "code_challenge", required = false) final String codeChallenge,
      final ModelMap model,
      final RedirectAttributes redirectAttributes) {

    final var response =
        identityService.create(
            request,
            responseType,
            state,
            redirectUri,
            clientId,
            codeChallengeMethod,
            codeChallenge);

    redirectAttributes.addAttribute("response_type", responseType);
    redirectAttributes.addAttribute("state", state);
    redirectAttributes.addAttribute("redirect_uri", redirectUri);
    redirectAttributes.addAttribute("client_id", clientId);
    redirectAttributes.addAttribute("code_challenge_method", codeChallengeMethod);
    redirectAttributes.addAttribute("code_challenge", codeChallenge);
    redirectAttributes.addFlashAttribute("X-IRIDIUM-AUTH-TOKEN", response.getUserToken());
    redirectAttributes.addFlashAttribute("appBaseUrl", response.getAppBaseUrl());
    redirectAttributes.addFlashAttribute("applicationName", response.getApplicationName());
    redirectAttributes.addFlashAttribute("tenantWebsite", response.getTenantWebsite());
    // todo: fix make authorization implicit redirect to client
    return new RedirectView("/authorize", true);
  }
}
