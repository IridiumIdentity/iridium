/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
