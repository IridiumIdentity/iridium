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

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.api.authentication.domain.IdentitySummaryResponse;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.base.domain.PagedListResponse;
import software.iridium.api.service.IdentityService;

@CrossOrigin
@RestController
public class IdentityController {

  @Autowired private IdentityService identityService;

  @RequestMapping(
      value = "/identities",
      method = RequestMethod.GET,
      produces = IdentityResponse.MEDIA_TYPE)
  public ApiDataResponse<IdentityResponse> getIdentity(HttpServletRequest request) {
    return new ApiDataResponse<>(identityService.getIdentity(request));
  }

  @GetMapping(
      value = "/tenants/{tenant-id}/identities",
      produces = IdentitySummaryResponse.MEDIA_TYPE_LIST)
  public PagedListResponse<IdentitySummaryResponse> getPage(
      final HttpServletRequest servletRequest,
      @PathVariable("tenant-id") final String tenantId,
      @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "20") final Integer size,
      @RequestParam(value = "active", defaultValue = "true") final Boolean active) {
    return identityService.getPage(servletRequest, tenantId, page, size, active);
  }
}
