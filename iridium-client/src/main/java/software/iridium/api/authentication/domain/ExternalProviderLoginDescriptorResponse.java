/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ExternalProviderLoginDescriptorResponse implements Serializable {
  private static final long serialVersionUID = 5012220113269851768L;

  private String name;

  private String clientId;

  private String responseType;

  private String redirectUri;

  private String state;

  private String scope;

  private String iconPath;

  public String getScope() {
    return scope;
  }

  public void setScope(final String scope) {
    this.scope = scope;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(final String iconPath) {
    this.iconPath = iconPath;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getResponseType() {
    return responseType;
  }

  public void setResponseType(final String responseType) {
    this.responseType = responseType;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(final String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }
}
