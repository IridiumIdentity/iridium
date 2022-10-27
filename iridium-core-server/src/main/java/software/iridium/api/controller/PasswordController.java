/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.controller;

import software.iridium.api.authentication.domain.InitiatePasswordResetRequest;
import software.iridium.api.authentication.domain.PasswordResetRequest;
import software.iridium.api.service.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;

@CrossOrigin
@RestController
public class PasswordController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    @Resource
    private PasswordService passwordService;

    @PostMapping(value = "/identities/initiate-reset-password", consumes = InitiatePasswordResetRequest.MEDIA_TYPE)
    public void initiateResetPassword(@RequestBody final InitiatePasswordResetRequest forgotPasswordRequest) {
        final var initiated = passwordService.initiatePasswordReset(forgotPasswordRequest);
        if (!initiated) {
            logger.info("failed attempt to initiate forgot password {}",
                    forgotPasswordRequest.getUsername());

        }
    }

    @PostMapping(value = "/reset-password", consumes = PasswordResetRequest.MEDIA_TYPE)
    public void resetPassword(@RequestBody PasswordResetRequest request) {
        passwordService.resetPassword(request);
    }

    @PostMapping(value = "/reset-password")
    public RedirectView resetWithFormSubmit(@ModelAttribute final PasswordResetRequest request,
                                            final ModelMap model,
                                            final RedirectAttributes redirectAttributes) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final var session = attr.getRequest().getSession(true);
        final var view = passwordService.resetPassword(request);
        return new RedirectView(view, true);

    }
}
