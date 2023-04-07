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
