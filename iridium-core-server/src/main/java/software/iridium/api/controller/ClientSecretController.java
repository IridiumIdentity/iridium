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
import software.iridium.api.authentication.domain.ClientSecretCreateResponse;
import software.iridium.api.base.domain.ApiResponse;
import software.iridium.api.service.ClientSecretService;

@CrossOrigin
@RestController
public class ClientSecretController {

  @Autowired private ClientSecretService clientSecretService;

  @PostMapping(
      value = "/tenants/{tenant-id}/applications/{application-id}/client-secrets",
      produces = ClientSecretCreateResponse.MEDIA_TYPE)
  public ClientSecretCreateResponse create(
      HttpServletRequest servletRequest,
      @PathVariable(name = "tenant-id") final String tenantId,
      @PathVariable(name = "application-id") final String applicationId) {

    return clientSecretService.create(servletRequest, tenantId, applicationId);
  }

  @DeleteMapping(value = "applications/{application-id}/client-secrets/{client-secret-id}")
  public ApiResponse delete(
      @PathVariable(name = "application-id") final String applicationId,
      @PathVariable(name = "client-secret-id") final String clientSecretId) {
    clientSecretService.delete(applicationId, clientSecretId);
    return new ApiResponse();
  }
}
