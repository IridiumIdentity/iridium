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
import software.iridium.api.authentication.domain.AuthenticationResponse;
import software.iridium.api.authentication.domain.IdentityResponse;
import software.iridium.entity.IdentityEntity;

@Component
public class IdentityResponseMapper {

  @Autowired private ProfileResponseMapper profileMapper;

  public IdentityResponse map(
      final IdentityEntity entity, final AuthenticationResponse authenticationResponse) {
    final var response = new IdentityResponse();
    response.setId(entity.getId());
    response.setUsername(entity.getPrimaryEmail().getEmailAddress());
    response.setProfile(profileMapper.map(entity.getProfile()));
    response.setAppBaseUrl(authenticationResponse.getAppBaseurl());
    response.setTenantWebsite(authenticationResponse.getTenantWebsite());
    response.setApplicationName(authenticationResponse.getApplicationName());
    response.setUserToken(authenticationResponse.getUserToken());
    return response;
  }

  public IdentityResponse map(final IdentityEntity entity) {
    final var response = new IdentityResponse();
    response.setId(entity.getId());
    response.setUsername(entity.getPrimaryEmail().getEmailAddress());
    response.setProfile(profileMapper.map(entity.getProfile()));
    return response;
  }
}
