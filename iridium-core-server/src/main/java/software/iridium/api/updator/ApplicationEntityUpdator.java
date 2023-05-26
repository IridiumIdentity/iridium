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
package software.iridium.api.updator;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.ApplicationUpdateRequest;
import software.iridium.entity.ApplicationEntity;
import software.iridium.entity.ApplicationTypeEntity;

@Component
public class ApplicationEntityUpdator {

  @Transactional(propagation = Propagation.REQUIRED)
  public ApplicationEntity update(
      final ApplicationEntity entity,
      final ApplicationTypeEntity applicationType,
      final ApplicationUpdateRequest request) {
    entity.setApplicationType(applicationType);
    entity.setRedirectUri(request.getRedirectUri());
    entity.setDescription(request.getDescription());
    entity.setName(request.getName());
    entity.setHomePageUrl(request.getHomePageUrl());
    entity.setPrivacyPolicyUrl(request.getPrivacyPolicyUrl());
    entity.setIconUrl(request.getIconUrl());
    return entity;
  }
}
