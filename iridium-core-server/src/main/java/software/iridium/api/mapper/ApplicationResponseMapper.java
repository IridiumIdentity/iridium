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
import software.iridium.api.authentication.domain.ApplicationResponse;
import software.iridium.api.entity.ApplicationEntity;

@Component
public class ApplicationResponseMapper {

  public ApplicationResponse map(final ApplicationEntity entity) {
    final var response = new ApplicationResponse();
    response.setName(entity.getName());
    response.setApplicationTypeId(entity.getApplicationType().getId());
    response.setTenantId(entity.getTenantId());
    response.setId(entity.getId());
    response.setDescription(entity.getDescription());
    response.setCallbackURL(entity.getRedirectUri());
    response.setHomepageURL(entity.getHomePageUrl());
    response.setPrivacyPolicyUrl(entity.getPrivacyPolicyUrl());
    return response;
  }
}
