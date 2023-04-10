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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import software.iridium.api.authentication.domain.ApplicationTypeSummary;
import software.iridium.api.base.domain.ApiListResponse;
import software.iridium.api.service.ApplicationTypeService;

@CrossOrigin
@RestController
public class ApplicationTypeController {

  @Resource private ApplicationTypeService applicationTypeService;

  @GetMapping(value = "/application-types", produces = ApplicationTypeSummary.MEDIA_TYPE_LIST)
  public ApiListResponse<ApplicationTypeSummary> getAll() {
    return new ApiListResponse<>(applicationTypeService.getAll());
  }
}
