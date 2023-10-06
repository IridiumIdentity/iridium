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
package software.iridium.entity.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import software.iridium.entity.IdentityEntity;

public class UserInfo extends User {

  @Serial private static final long serialVersionUID = -1300015092363456591L;

  public UserInfo(IdentityEntity identity) {
    super(String.valueOf(identity.getId()), "", new ArrayList<>());
  }

  public UserInfo(
      IdentityEntity identity, Collection<? extends GrantedAuthority> grantedAuthorities) {
    super(String.valueOf(identity.getId()), "", grantedAuthorities);
  }
}
