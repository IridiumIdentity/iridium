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
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderRequest;
import software.iridium.api.authentication.domain.CreateExternalIdentityProviderResponse;
import software.iridium.api.service.ExternalIdentityProviderService;

@CrossOrigin
@RestController
public class ExternalIdentityProviderController {

  @Autowired private ExternalIdentityProviderService providerService;

  @PostMapping(
      value = "tenants/{tenant-id}/external-providers",
      produces = CreateExternalIdentityProviderResponse.MEDIA_TYPE,
      consumes = CreateExternalIdentityProviderRequest.MEDIA_TYPE)
  public CreateExternalIdentityProviderResponse create(
      @PathVariable("tenant-id") final String tenantId,
      @RequestBody final CreateExternalIdentityProviderRequest request) {
    return providerService.create(tenantId, request);
  }
}
