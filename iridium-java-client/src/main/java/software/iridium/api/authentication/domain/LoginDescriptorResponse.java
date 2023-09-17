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
import java.util.List;

public class LoginDescriptorResponse implements Serializable {

  private static final long serialVersionUID = -672573685676065352L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.login-descriptor-response.1+json";

  private String displayName;

  private String usernamePlaceholder;

  private String usernameLabel;

  private String usernameType;

  private String usernameErrorHint;

  private String pageTitle;

  private String tenantLogoUrl;

  private List<ExternalProviderLoginDescriptorResponse> externalProviderDescriptors;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  public String getUsernamePlaceholder() {
    return usernamePlaceholder;
  }

  public void setUsernamePlaceholder(final String usernamePlaceholder) {
    this.usernamePlaceholder = usernamePlaceholder;
  }

  public String getUsernameLabel() {
    return usernameLabel;
  }

  public void setUsernameLabel(final String usernameLabel) {
    this.usernameLabel = usernameLabel;
  }

  public String getUsernameType() {
    return usernameType;
  }

  public void setUsernameType(final String usernameType) {
    this.usernameType = usernameType;
  }

  public String getUsernameErrorHint() {
    return usernameErrorHint;
  }

  public void setUsernameErrorHint(final String usernameErrorHint) {
    this.usernameErrorHint = usernameErrorHint;
  }

  public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(final String pageTitle) {
    this.pageTitle = pageTitle;
  }

  public String getTenantLogoUrl() {
    return tenantLogoUrl;
  }

  public void setTenantLogoUrl(final String tenantLogoUrl) {
    this.tenantLogoUrl = tenantLogoUrl;
  }

  public List<ExternalProviderLoginDescriptorResponse> getExternalProviderDescriptors() {
    return externalProviderDescriptors;
  }

  public void setExternalProviderDescriptors(
      final List<ExternalProviderLoginDescriptorResponse> externalProviderDescriptors) {
    this.externalProviderDescriptors = externalProviderDescriptors;
  }

  @Override
  public String toString() {
    return "LoginDescriptorResponse{"
        + "displayName='"
        + displayName
        + '\''
        + ", usernamePlaceholder='"
        + usernamePlaceholder
        + '\''
        + ", usernameLabel='"
        + usernameLabel
        + '\''
        + ", usernameType='"
        + usernameType
        + '\''
        + ", usernameErrorHint='"
        + usernameErrorHint
        + '\''
        + ", pageTitle='"
        + pageTitle
        + '\''
        + ", tenantLogoUrl='"
        + tenantLogoUrl
        + '\''
        + ", externalProviderDescriptors="
        + externalProviderDescriptors
        + '}';
  }
}
