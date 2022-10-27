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
package software.iridium.api.instantiator;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.GithubProfileResponse;
import software.iridium.api.entity.IdentityEntity;
import software.iridium.api.entity.IdentityPropertyEntity;

@Component
public class IdentityPropertyEntityInstantiator {

  public static final String GITHUB_AVATAR_URL_KEY = "avatarUrl";
  public static final String GITHUB_LOGIN_KEY = "login";

  @Transactional(propagation = Propagation.REQUIRED)
  public void instantiateGithubProperties(
      final GithubProfileResponse response, final IdentityEntity identity) {
    final var avatarUrlProperty = new IdentityPropertyEntity();

    // todo: think about how we can do this better
    avatarUrlProperty.setIdentity(identity);
    avatarUrlProperty.setName(GITHUB_AVATAR_URL_KEY);
    avatarUrlProperty.setValue(response.getAvatarUrl());
    identity.getIdentityProperties().add(avatarUrlProperty);

    final var loginProperty = new IdentityPropertyEntity();
    loginProperty.setIdentity(identity);
    loginProperty.setValue(response.getLogin());
    loginProperty.setName(GITHUB_LOGIN_KEY);
    identity.getIdentityProperties().add(loginProperty);
  }
}
