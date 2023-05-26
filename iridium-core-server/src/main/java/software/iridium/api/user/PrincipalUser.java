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
package software.iridium.api.user;

import java.security.Principal;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class PrincipalUser extends User implements Principal {
  private static final long serialVersionUID = -3997242419392271736L;

  private String emailAddress;
  private String authToken;

  public PrincipalUser(
      final String authToken, final String emailAddress, final List<GrantedAuthority> authorities) {
    super(emailAddress, "password", authorities);
    this.authToken = authToken;
  }

  @Override
  public String getName() {
    return emailAddress;
  }

  public String getAuthToken() {
    return authToken;
  }
}
