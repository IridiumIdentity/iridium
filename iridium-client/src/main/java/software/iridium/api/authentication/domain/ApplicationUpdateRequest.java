/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationUpdateRequest implements Serializable {

  private String name;

  private String description;

  private String homePageUrl;

  private String privacyPolicyUrl;

  private String redirectUri;

  private String iconUrl;

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

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(final String iconUrl) {
    this.iconUrl = iconUrl;
  }
}
