/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
