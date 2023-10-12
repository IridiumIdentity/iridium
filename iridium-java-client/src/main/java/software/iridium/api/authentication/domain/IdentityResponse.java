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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IdentityResponse implements Serializable {

  public static final String MEDIA_TYPE = "application/vnd.iridium.id.identity-response.1+json";

  private static final long serialVersionUID = 7221668651862331716L;

  private String id;

  private String username;

  private ProfileResponse profile;

  @JsonIgnore private String redirectUri;

  private Set<String> roles = new HashSet<>();

  private List<String> tenantIds = new ArrayList<>();

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(final Set<String> roles) {
    this.roles = roles;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public ProfileResponse getProfile() {
    return profile;
  }

  public void setProfile(final ProfileResponse profile) {
    this.profile = profile;
  }

  public List<String> getTenantIds() {
    return tenantIds;
  }

  public void setTenantIds(final List<String> tenantIds) {
    this.tenantIds = tenantIds;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(final String redirectUri) {
    this.redirectUri = redirectUri;
  }

  @Override
  public String toString() {
    return "IdentityResponse{"
        + "id='"
        + id
        + '\''
        + ", username='"
        + username
        + '\''
        + ", profile="
        + profile
        + '\''
        + ", roles="
        + roles
        + ", tenantIds="
        + tenantIds
        + '}';
  }
}
