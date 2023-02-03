/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.iridium.api.authentication.domain.ProviderSummaryResponse;
import software.iridium.api.base.domain.ApiListResponse;
import software.iridium.api.service.ProviderService;

@CrossOrigin
@RestController
public class ProviderController {

  @Resource private ProviderService providerService;

  @GetMapping(value = "providers", produces = ProviderSummaryResponse.MEDIA_TYPE_LIST)
  public ApiListResponse<ProviderSummaryResponse> retrieveAllSummaries() {
    return new ApiListResponse<>(providerService.retrieveAllSummaries());
  }
}
