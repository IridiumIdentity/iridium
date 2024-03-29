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
package software.iridium.entity;

import jakarta.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "login_descriptor_id"))
@Table(name = "login_descriptors")
public class LoginDescriptorEntity extends AbstractEntity {
  private static final long serialVersionUID = -4875102248476691639L;

  @Column(name = "display_name", length = 100, nullable = true)
  private String displayName;

  @Column(name = "username_placeholder", length = 255, nullable = false)
  private String usernamePlaceholder = "Ex. you@somewhere.com";

  @Column(name = "username_label", length = 255, nullable = false)
  private String usernameLabel = "Email";

  @Column(name = "username_type", length = 255, nullable = false)
  private String usernameType = "email";

  @Column(name = "username_error_hint", length = 255, nullable = false)
  private String usernameErrorHint = "Please enter a valid email address";

  @Column(name = "page_title", length = 50, nullable = false)
  private String pageTitle = "Login | Powered by Iridium";

  @Column(name = "logo_url", length = 2048, nullable = false)
  private String logoURL = "";

  @OneToOne(mappedBy = "loginDescriptor", optional = false)
  private TenantEntity tenant;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(final String tenantName) {
    this.displayName = tenantName;
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

  public String getLogoURL() {
    return logoURL;
  }

  public void setLogoURL(final String iconPath) {
    this.logoURL = iconPath;
  }

  public TenantEntity getTenant() {
    return tenant;
  }

  public void setTenant(final TenantEntity tenant) {
    this.tenant = tenant;
  }
}
