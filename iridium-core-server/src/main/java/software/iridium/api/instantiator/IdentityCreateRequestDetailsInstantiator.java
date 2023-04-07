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

import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import software.iridium.api.authentication.domain.CodeChallengeMethod;
import software.iridium.api.entity.IdentityCreateSessionDetails;
import software.iridium.api.entity.IdentityEntity;

@Component
public class IdentityCreateRequestDetailsInstantiator {

  @Transactional(propagation = Propagation.REQUIRED)
  public IdentityCreateSessionDetails instantiate(
      final Map<String, String> requestParamMap, final IdentityEntity identity) {
    final var entity = new IdentityCreateSessionDetails();
    entity.setCodeChallenge(requestParamMap.get("code_challenge"));
    entity.setCodeChallengeMethod(
        CodeChallengeMethod.valueOf(requestParamMap.get("code_challenge_method")));
    entity.setClientId(requestParamMap.get("client_id"));
    entity.setState(requestParamMap.get("state"));
    entity.setResponseType(requestParamMap.get("response_type"));
    entity.setRedirectUri(requestParamMap.get("redirect_uri"));
    entity.setIdentity(identity);
    return entity;
  }
}
