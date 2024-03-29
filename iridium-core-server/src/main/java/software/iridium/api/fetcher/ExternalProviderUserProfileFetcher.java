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
package software.iridium.api.fetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.AuthorizationResponse;
import software.iridium.api.authentication.domain.ExternalProviderProfile;
import software.iridium.entity.ExternalIdentityProviderEntity;

@Component
public class ExternalProviderUserProfileFetcher {

  @Autowired private GitHubProfileFetcher gitHubProfileFetcher;
  @Autowired private GoogleProfileFetcher googleProfileFetcher;

  public ExternalProviderProfile fetch(
      final ExternalIdentityProviderEntity provider, final AuthorizationResponse response) {

    if (provider.getName().equalsIgnoreCase("github")) {
      return gitHubProfileFetcher.requestGithubProfile(
          provider.getProfileRequestBaseUrl(), response.getAccessToken());
    }
    if (provider.getName().equalsIgnoreCase("google")) {
      return googleProfileFetcher.fetch(provider, response.getAccessToken());
    }
    throw new RuntimeException("unexpected provider: " + provider.getName());
  }
}
