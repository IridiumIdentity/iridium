/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import software.iridium.api.authentication.domain.ApplicationCreateRequest;
import software.iridium.api.authentication.domain.ApplicationCreateResponse;
import software.iridium.api.authentication.domain.ApplicationSummary;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.base.domain.PagedListResponse;
import software.iridium.api.service.ApplicationService;

@CrossOrigin
@RestController
public class ApplicationController {

  @Resource private ApplicationService applicationService;

  @PostMapping(
      value = "/tenants/{tenant-id}/applications",
      consumes = ApplicationCreateRequest.MEDIA_TYPE,
      produces = ApplicationCreateResponse.MEDIA_TYPE)
  public ApiDataResponse<ApplicationCreateResponse> create(
      @RequestBody final ApplicationCreateRequest request,
      @PathVariable("tenant-id") final String tenantId) {
    return new ApiDataResponse<>(applicationService.create(request, tenantId));
  }

  @GetMapping(
      value = "/tenants/{tenant-id}/applications/{application-type-id}",
      produces = ApplicationSummary.MEDIA_TYPE_LIST)
  public PagedListResponse<ApplicationSummary> getPageByTenantAndApplicationType(
      @PathVariable("tenant-id") final String tenantId,
      @PathVariable("application-type-id") final String applicationTypeId,
      @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "20") final Integer size,
      @RequestParam(value = "active", defaultValue = "true") final Boolean active) {
    return applicationService.getPageByTenantIdAndApplicationTypeId(
        tenantId, applicationTypeId, page, size, active);
  }
}
