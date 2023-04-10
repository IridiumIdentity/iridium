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

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.entity.ExternalIdentityProviderEntity;
import software.iridium.api.entity.InProgressExternalIdentityProviderAuthorizationEntity;

@Component
public class InProgressExternalIdentityProviderAuthorizationInstantiator {

  @Transactional(propagation = Propagation.REQUIRED)
  public InProgressExternalIdentityProviderAuthorizationEntity instantiate(
      final ExternalIdentityProviderEntity provider,
      final String state,
      final String redirectUri,
      final String clientId) {
    final var entity = new InProgressExternalIdentityProviderAuthorizationEntity();
    entity.setProvider(provider);
    entity.setClientId(clientId);
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.MINUTE, 10);
    entity.setExpiration(c.getTime());
    entity.setState(state);
    entity.setRedirectUri(redirectUri);
    return entity;
  }
}
