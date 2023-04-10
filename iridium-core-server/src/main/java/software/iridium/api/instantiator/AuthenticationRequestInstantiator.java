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
package software.iridium.api.instantiator;

import org.springframework.stereotype.Component;
import software.iridium.api.authentication.domain.AuthenticationRequest;
import software.iridium.api.authentication.domain.CreateIdentityRequest;

@Component
public class AuthenticationRequestInstantiator {

  public AuthenticationRequest instantiate(final CreateIdentityRequest request) {
    final var authenticationRequest = new AuthenticationRequest();
    authenticationRequest.setUsername(request.getUsername());
    authenticationRequest.setPassword(request.getPassword());
    authenticationRequest.setClientId(request.getClientId());
    return authenticationRequest;
  }
}
