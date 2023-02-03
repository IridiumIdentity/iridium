/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import java.util.HashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import software.iridium.api.authentication.domain.CreateIdentityRequest;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.service.IdentityService;

@CrossOrigin
@RestController
public class IdentityController {

  @Resource private IdentityService identityService;

  @RequestMapping(
      value = "/tenants/{tenant-id}/identities",
      method = RequestMethod.GET,
      produces = IdentityResponse.MEDIA_TYPE)
  public ApiDataResponse<IdentityResponse> getIdentity(HttpServletRequest request) {
    return new ApiDataResponse<>(identityService.getIdentity(request));
  }

  @PostMapping(value = "/identities")
  public RedirectView createWithFormSubmit(
      @ModelAttribute final CreateIdentityRequest request,
      @RequestParam(value = "response_type", required = false) final String responseType,
      @RequestParam(value = "state", required = false) final String state,
      @RequestParam(value = "redirect_uri", required = false) final String redirectUri,
      @RequestParam(value = "client_id", required = false) final String clientId,
      @RequestParam(value = "code_challenge_method", required = false)
          final String codeChallengeMethod,
      @RequestParam(value = "code_challenge", required = false) final String codeChallenge,
      final ModelMap model,
      final RedirectAttributes redirectAttributes) {
    final var paramMap = new HashMap<String, String>();
    paramMap.put("response_type", responseType);
    paramMap.put("state", state);
    paramMap.put("redirect_uri", redirectUri);
    paramMap.put("client_id", clientId);
    paramMap.put("code_challenge_method", codeChallengeMethod);
    paramMap.put("code_challenge", codeChallenge);
    final var response = identityService.create(request, paramMap);

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

    return new RedirectView("/authorize", true);
  }
}
