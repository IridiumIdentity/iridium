/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;
import java.util.List;

public class LoginDescriptorResponse implements Serializable {

  private static final long serialVersionUID = -672573685676065352L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.authn.login-descriptor-response.1+json";

  private String displayName;

  private String usernamePlaceholder;

  private String usernameLabel;

  private String usernameType;

  private String usernameErrorHint;

  private String pageTitle;

  private String iconPath;

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

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(final String iconPath) {
    this.iconPath = iconPath;
  }

  public List<ExternalProviderLoginDescriptorResponse> getExternalProviderDescriptors() {
    return externalProviderDescriptors;
  }

  public void setExternalProviderDescriptors(
      final List<ExternalProviderLoginDescriptorResponse> externalProviderDescriptors) {
    this.externalProviderDescriptors = externalProviderDescriptors;
  }
}
