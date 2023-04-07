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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class AccessTokenResponse implements Serializable {

  private static final long serialVersionUID = -7428472367312817396L;

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("token_type")
  private String tokenType;

  @JsonProperty("expires_in")
  private Long expiresIn;

  @JsonIgnore private String redirectUrl;

  @JsonIgnore private boolean error;

  public AccessTokenResponse() {}

  private AccessTokenResponse(final String redirectUrl) {
    this.redirectUrl = redirectUrl;
    this.error = true;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(final String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(final String tokenType) {
    this.tokenType = tokenType;
  }

  public Long getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(final Long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(final String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public boolean hasError() {
    return error;
  }

  public static AccessTokenResponse withError(final String redirectUri) {
    return new AccessTokenResponse(redirectUri);
  }
}
