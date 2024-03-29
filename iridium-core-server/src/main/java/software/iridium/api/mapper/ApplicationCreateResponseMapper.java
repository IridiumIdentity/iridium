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
package software.iridium.api.mapper;

import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.ApplicationCreateResponse;
import software.iridium.entity.ApplicationEntity;

@Component
public class ApplicationCreateResponseMapper {

  public ApplicationCreateResponse map(final ApplicationEntity entity) {
    final var response = new ApplicationCreateResponse();
    response.setName(entity.getName());
    response.setApplicationTypeId(entity.getApplicationType().getId());
    response.setTenantId(entity.getTenantId());
    response.setId(entity.getId());
    return response;
  }
}
