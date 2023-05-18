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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.iridium.api.authentication.domain.CreateTenantRequest;
import software.iridium.api.authentication.domain.CreateTenantResponse;
import software.iridium.api.authentication.domain.TenantSummary;
import software.iridium.api.service.TenantService;

@CrossOrigin
@RestController
public class TenantController {

  private static final Logger logger = LoggerFactory.getLogger(TenantController.class);

  @Autowired private TenantService tenantService;

  @GetMapping(value = "tenants", produces = TenantSummary.MEDIA_TYPE_LIST)
  public List<TenantSummary> getTenantSummaries(HttpServletRequest request) {
    logger.info("get summaries");
    return tenantService.getTenantSummaries(request);
  }


  @PostMapping(
      value = "tenants",
      consumes = CreateTenantRequest.MEDIA_TYPE,
      produces = CreateTenantResponse.MEDIA_TYPE)
  public CreateTenantResponse create(
      final HttpServletRequest request, @RequestBody final CreateTenantRequest tenantRequest) {
    return tenantService.create(request, tenantRequest);
  }
}
