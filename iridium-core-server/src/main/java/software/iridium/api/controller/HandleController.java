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
import software.iridium.api.service.HandleService;

@CrossOrigin
@RestController
public class HandleController {

  @Autowired private HandleService handleService;

  @GetMapping(value = "/handles/{tenant-id}/verifications")
  public void verifyUserHandle(
      @PathVariable(name = "tenant-id") final String tenantId,
      @RequestParam(value = "token") final String token) {
    handleService.verify(tenantId, token);
  }
}
