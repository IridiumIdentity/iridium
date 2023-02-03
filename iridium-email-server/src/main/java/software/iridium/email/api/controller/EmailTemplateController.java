/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.email.domain.EmailTemplateResponse;
import software.iridium.email.api.service.EmailTemplateService;

@CrossOrigin
@RestController
public class EmailTemplateController {

  @Resource private EmailTemplateService templateService;

  @GetMapping(
      value = "/email-templates/{email-template-id}",
      produces = EmailTemplateResponse.MEDIA_TYPE)
  public ApiDataResponse<EmailTemplateResponse> get(final String emailTemplateId) {
    return new ApiDataResponse<>(templateService.get(emailTemplateId));
  }
}
