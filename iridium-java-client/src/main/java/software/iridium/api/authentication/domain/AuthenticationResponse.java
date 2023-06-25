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

public class AuthenticationResponse implements Serializable {

  private static final long serialVersionUID = -8917607474548353606L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.authentication-response.1+json";

  private String userToken;

  private String userRefreshToken;

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

  public AuthenticationResponse() {}

  public String getUserToken() {
    return userToken;
  }

  public String getUserRefreshToken() {
    return userRefreshToken;
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
