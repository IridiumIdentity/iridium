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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.model.AuthorizationRequestHolder;
import software.iridium.entity.IdentityCreateSessionDetails;
import software.iridium.entity.IdentityEntity;

@Component
public class IdentityCreateRequestDetailsInstantiator {

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityCreateSessionDetails instantiate(
      final AuthorizationRequestHolder holder, final IdentityEntity identity) {
    final var entity = new IdentityCreateSessionDetails();
    entity.setCodeChallenge(holder.getCodeChallenge());
    entity.setCodeChallengeMethod(CodeChallengeMethod.valueOf(holder.getCodeChallengeMethod()));
    entity.setClientId(holder.getClientId());
    entity.setState(holder.getState());
    entity.setResponseType(holder.getResponseType());
    entity.setRedirectUri(holder.getRedirectUri());
    entity.setIdentity(identity);
    return entity;
  }
}
