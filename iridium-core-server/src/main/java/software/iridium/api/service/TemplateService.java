/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.service;

import software.iridium.api.util.SubdomainExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class TemplateService {

    @Resource
    private SubdomainExtractor subdomainExtractor;
    @Resource
    private LoginDescriptorService loginDescriptorService;

    @Transactional(propagation = Propagation.SUPPORTS)
    public String describeIndex(final Model model, final HttpServletRequest servletRequest, final Map<String,  String> params) {

        final var subdomain = subdomainExtractor.extract(servletRequest.getRequestURL().toString());

        final var loginDescriptor = loginDescriptorService.getBySubdomain(subdomain);

        model.addAttribute("displayName", loginDescriptor.getDisplayName());
        model.addAttribute("externalProviderDescriptors", loginDescriptor.getExternalProviderDescriptors());

        return "index";
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public String describeRegister(final Model model, final HttpServletRequest servletRequest) {
        final var subdomain = subdomainExtractor.extract(servletRequest.getRequestURL().toString());

        final var loginDescriptor = loginDescriptorService.getBySubdomain(subdomain);

        model.addAttribute("displayName", loginDescriptor.getDisplayName());
//        model.addAttribute("allowGithub", loginDescriptor.getAllowGithub());
//        model.addAttribute("allowGoogle", loginDescriptor.getAllowGoogle());
//        model.addAttribute("allowApple", loginDescriptor.getAllowApple());
//        model.addAttribute("allowMicrosoft", loginDescriptor.getAllowMicrosoft());

        return "register";
    }
}
