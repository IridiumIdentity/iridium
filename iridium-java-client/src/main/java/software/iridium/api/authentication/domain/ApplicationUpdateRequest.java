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
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationUpdateRequest implements Serializable {

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.application-update-request.1+json";

  private String name;

  private String description;

  private String homePageUrl;

  private String privacyPolicyUrl;

  private String redirectUri;

  private String applicationTypeId;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getHomePageUrl() {
    return homePageUrl;
  }

  public void setHomePageUrl(final String homePageUrl) {
    this.homePageUrl = homePageUrl;
  }

  public String getPrivacyPolicyUrl() {
    return privacyPolicyUrl;
  }

  public void setPrivacyPolicyUrl(final String privacyPolicyUrl) {
    this.privacyPolicyUrl = privacyPolicyUrl;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(final String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getApplicationTypeId() {
    return applicationTypeId;
  }

  public void setApplicationTypeId(final String applicationTypeId) {
    this.applicationTypeId = applicationTypeId;
  }
}
