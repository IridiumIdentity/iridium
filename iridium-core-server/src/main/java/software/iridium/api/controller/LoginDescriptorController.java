/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.service.LoginDescriptorService;

@CrossOrigin
@RestController
public class LoginDescriptorController {

  @Resource private LoginDescriptorService descriptorService;

  @GetMapping(
      value = "login-descriptors/{subdomain}",
      produces = LoginDescriptorResponse.MEDIA_TYPE)
  public ApiDataResponse<LoginDescriptorResponse> getBySubdomain(
      @PathVariable(value = "subdomain") final String subdomain) {
    return new ApiDataResponse<>(descriptorService.getBySubdomain(subdomain));
  }
}
