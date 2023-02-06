/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.email.domain.EmailSendRequest;
import software.iridium.api.email.domain.EmailSendResponse;
import software.iridium.email.api.service.EmailService;

@CrossOrigin
@RestController
public class EmailController {

  @Resource private EmailService emailService;

  @PostMapping(
      path = "/emails",
      consumes = EmailSendRequest.MEDIA_TYPE,
      produces = EmailSendResponse.MEDIA_TYPE)
  public ApiDataResponse<EmailSendResponse> send(@RequestBody final EmailSendRequest request) {
    // return new ApiDataResponse<>(emailService.send(request));
    return new ApiDataResponse<>(null);
  }
}
