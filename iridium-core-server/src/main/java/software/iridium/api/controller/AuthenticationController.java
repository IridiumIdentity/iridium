/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.controller;

import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@CrossOrigin
@RestController
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Resource
    private AuthenticationService authenticationService;

    @PostMapping(value = "/authenticate")
    public RedirectView authenticateWithFormSubmit(
            @ModelAttribute AuthenticationRequest request,
            ModelMap model,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "response_type", required = false) final String responseType,
            @RequestParam(value = "state", required = false) final String state,
            @RequestParam(value = "redirect_uri", required = false) final String redirectUri,
            @RequestParam(value = "client_id", required = false) final String clientId,
            @RequestParam(value = "code_challenge_method", required = false) final String codeChallengeMethod,
            @RequestParam(value = "code_challenge", required = false) final String codeChallenge,
            HttpServletRequest servletRequest) {

        logger.info("query String {}", servletRequest.getQueryString());
        logger.info("request url {}", servletRequest.getRequestURL().toString());
        final var paramMap = addToMap(responseType, state, redirectUri, clientId, codeChallengeMethod, codeChallenge);

        final var response = authenticationService.authenticate(request, paramMap);
        if (response.getUserToken() != null) {
            if (response.applicationIsAuthorized()) {

                redirectAttributes.addAttribute("code", response.getAuthorizationCode());
                redirectAttributes.addAttribute("state", state);
                return new RedirectView(response.getApplicationRedirectUrl());
            }
            redirectAttributes.addAttribute("response_type", responseType);
            redirectAttributes.addAttribute("state", state);
            redirectAttributes.addAttribute("redirect_uri", redirectUri);
            redirectAttributes.addAttribute("client_id", clientId);
            redirectAttributes.addAttribute("code_challenge_method", codeChallengeMethod);
            redirectAttributes.addAttribute("code_challenge", codeChallenge);
            redirectAttributes.addFlashAttribute("X-IRIDIUM-AUTH-TOKEN", response.getUserToken());
            redirectAttributes.addFlashAttribute("appBaseUrl", response.getAppBaseurl());
            redirectAttributes.addFlashAttribute("applicationName", response.getApplicationName());
            redirectAttributes.addFlashAttribute("tenantWebsite", response.getTenantWebsite());

            return new RedirectView("/authorize", true);
        }
        if (response.getPasswordResetLink() != null) {
            return new RedirectView(response.getPasswordResetLink(), true);
        }
        return new RedirectView("/error", true);
    }

    private HashMap<String, String> addToMap(final String responseType, final String state, final String redirectUri, final String clientId, final String codeChallengeMethod, final String codeChallenge) {
        final var paramMap = new HashMap<String, String>();
        paramMap.put("response_type", responseType);
        paramMap.put("state", state);
        paramMap.put("redirect_uri", redirectUri);
        paramMap.put("client_id", clientId);
        paramMap.put("code_challenge_method", codeChallengeMethod);
        paramMap.put("code_challenge", codeChallenge);
        return paramMap;
    }
}
