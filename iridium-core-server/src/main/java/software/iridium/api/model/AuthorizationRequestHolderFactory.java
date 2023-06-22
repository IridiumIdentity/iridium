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
package software.iridium.api.model;

import java.util.Map;
import software.iridium.api.util.AuthorizationCodeFlowConstants;

public class AuthorizationRequestHolderFactory {
  /** Default no-arg constructor */
  public AuthorizationRequestHolderFactory() {}

  public AuthorizationRequestHolder createAuthorizationRequestHolder(Map<String, String> params) {
    AuthorizationRequestHolder holder = new AuthorizationRequestHolder();
    holder.setClientId(params.get(AuthorizationCodeFlowConstants.CLIENT_ID.getValue()));
    holder.setCodeChallenge(params.get(AuthorizationCodeFlowConstants.CODE_CHALLENGE.getValue()));
    holder.setCodeChallengeMethod(
        params.get(AuthorizationCodeFlowConstants.CODE_CHALLENGE_METHOD.getValue()));
    holder.setRedirectUri(params.get(AuthorizationCodeFlowConstants.REDIRECT_URI.getValue()));
    holder.setResponseType(params.get(AuthorizationCodeFlowConstants.RESPONSE_TYPE.getValue()));
    holder.setState(params.get(AuthorizationCodeFlowConstants.STATE.getValue()));
    holder.setParams(params);

    return holder;
  }
}
