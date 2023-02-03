/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.CreateTenantResponse;
import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.base.domain.ApiListResponse;
import software.iridium.api.service.TenantService;

@CrossOrigin
@RestController
public class TenantController {

  @Resource private TenantService tenantService;

  @GetMapping(value = "tenants", produces = TenantSummary.MEDIA_TYPE_LIST)
  public ApiListResponse<TenantSummary> getTenantSummaries(HttpServletRequest request) {
    return new ApiListResponse<>(tenantService.getTenantSummaries(request));
  }

  @PostMapping(
      value = "tenants",
      consumes = CreateTenantRequest.MEDIA_TYPE,
      produces = CreateTenantResponse.MEDIA_TYPE)
  public ApiDataResponse<CreateTenantResponse> create(
      final HttpServletRequest request, @RequestBody final CreateTenantRequest tenantRequest) {
    return new ApiDataResponse<>(tenantService.create(request, tenantRequest));
  }
}
