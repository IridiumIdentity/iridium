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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.authentication.domain.TenantLogoUrlUpdateRequest;
import software.iridium.api.authentication.domain.TenantLogoUrlUpdateResponse;
import software.iridium.api.base.domain.ApiDataResponse;
import software.iridium.api.service.LoginDescriptorService;

@CrossOrigin
@RestController
public class LoginDescriptorController {

  @Autowired private LoginDescriptorService descriptorService;

  @GetMapping(
      value = "login-descriptors/{subdomain}",
      produces = LoginDescriptorResponse.MEDIA_TYPE)
  public ApiDataResponse<LoginDescriptorResponse> getBySubdomain(
      @PathVariable(value = "subdomain") final String subdomain) {
    return new ApiDataResponse<>(descriptorService.getBySubdomain(subdomain));
  }

  @PutMapping(
      value = "tenants/{tenant-id}/login-descriptors",
      consumes = TenantLogoUrlUpdateRequest.MEDIA_TYPE,
      produces = TenantLogoUrlUpdateResponse.MEDIA_TYPE)
  public TenantLogoUrlUpdateResponse updateLogoURL(
      @RequestBody TenantLogoUrlUpdateRequest request,
      @PathVariable(value = "tenant-id") final String tenantId) {

    return descriptorService.updateLogoURL(request, tenantId);
  }
}
