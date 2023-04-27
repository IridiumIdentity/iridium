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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.LoginDescriptorResponse;
import software.iridium.api.entity.LoginDescriptorEntity;

@Component
public class LoginDescriptorResponseMapper {

  @Autowired ExternalProviderLoginDescriptorResponseMapper externalProviderDescriptorMapper;

  public LoginDescriptorResponse map(final LoginDescriptorEntity entity) {

    final var response = new LoginDescriptorResponse();
    response.setExternalProviderDescriptors(
        externalProviderDescriptorMapper.mapList(
            entity.getTenant().getExternalIdentityProviders()));
    response.setIconPath(entity.getIconPath());
    response.setPageTitle(entity.getPageTitle());
    response.setDisplayName(entity.getDisplayName());
    response.setUsernameLabel(entity.getUsernameLabel());
    response.setUsernameErrorHint(entity.getUsernameErrorHint());
    response.setUsernameType(entity.getUsernameType());
    response.setUsernamePlaceholder(entity.getUsernamePlaceholder());
    return response;
  }
}
