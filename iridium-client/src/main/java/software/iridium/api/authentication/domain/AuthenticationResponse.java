/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

  private static final long serialVersionUID = -8917607474548353606L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.authentication-response.1+json";

  private String userToken;

  private String userRefreshToken;

  private String passwordResetLink;

  private boolean applicationIsAuthorized;

  private String applicationName;

  private String tenantWebsite;

  private String appBaseurl;

  private String applicationRedirectUrl;

  private String authorizationCode;

  private AuthenticationResponse(
      final String userToken,
      final String userRefreshToken,
      final Boolean applicationIsAuthorized) {
    this.userToken = userToken;
    this.userRefreshToken = userRefreshToken;
    this.applicationIsAuthorized = applicationIsAuthorized;
  }

  private AuthenticationResponse(
      final String userToken,
      final String userRefreshToken,
      final boolean applicationIsAuthorized,
      final String applicationName,
      final String tenantWebsite,
      final String appBaseurl,
      final String applicationRedirectUrl,
      final String authorizationCode) {
    this.userToken = userToken;
    this.userRefreshToken = userRefreshToken;
    this.applicationIsAuthorized = applicationIsAuthorized;
    this.applicationName = applicationName;
    this.tenantWebsite = tenantWebsite;
    this.appBaseurl = appBaseurl;
    this.applicationRedirectUrl = applicationRedirectUrl;
    this.authorizationCode = authorizationCode;
  }

  public AuthenticationResponse(final String passwordResetLink) {
    this.passwordResetLink = passwordResetLink;
  }

  public AuthenticationResponse() {}

  public String getUserToken() {
    return userToken;
  }

  public String getUserRefreshToken() {
    return userRefreshToken;
  }

  public String getPasswordResetLink() {
    return passwordResetLink;
  }

  public boolean applicationIsAuthorized() {
    return applicationIsAuthorized;
  }

  public boolean isApplicationIsAuthorized() {
    return applicationIsAuthorized;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public String getTenantWebsite() {
    return tenantWebsite;
  }

  public String getAppBaseurl() {
    return appBaseurl;
  }

  public void setApplicationIsAuthorized(final boolean applicationIsAuthorized) {
    this.applicationIsAuthorized = applicationIsAuthorized;
  }

  public String getAuthorizationCode() {
    return authorizationCode;
  }

  public static AuthenticationResponse of(
      final String userToken, final String userRefreshToken, final Boolean isAuthorized) {
    return new AuthenticationResponse(userToken, userRefreshToken, isAuthorized);
  }

  public static AuthenticationResponse of(final String passwordResetLink) {
    return new AuthenticationResponse(passwordResetLink);
  }

  public String getApplicationRedirectUrl() {
    return applicationRedirectUrl;
  }

  public static AuthenticationResponse of(
      final String userToken,
      final String userRefreshToken,
      final Boolean isAuthorized,
      final String applicationName,
      final String tenantWebsite,
      final String appBaseurl,
      final String applicationRedirectUrl,
      final String authorizationCode) {
    return new AuthenticationResponse(
        userToken,
        userRefreshToken,
        isAuthorized,
        applicationName,
        tenantWebsite,
        appBaseurl,
        applicationRedirectUrl,
        authorizationCode);
  }
}
