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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class OpenIDAuthenticationRequest implements Serializable {

  private static final long serialVersionUID = -2241796820066775989L;
  // required
  private String scope;

  // required
  @JsonProperty("response_type")
  private String response_type;

  // required
  @JsonProperty("client_id")
  private String client_id;

  // required
  @JsonProperty("redirect_uri")
  private String redirect_uri;

  // required
  @JsonProperty("state")
  private String state;

  @JsonProperty("code_challenge")
  private String code_challenge;

  @JsonProperty("code_challenge_method")
  private String code_challenge_method;

  public String getScope() {
    return scope;
  }

  public void setScope(final String scope) {
    this.scope = scope;
  }

  public String getResponse_type() {
    return response_type;
  }

  public void setResponse_type(final String response_type) {
    this.response_type = response_type;
  }

  public String getClient_id() {
    return client_id;
  }

  public void setClient_id(final String client_id) {
    this.client_id = client_id;
  }

  public String getRedirect_uri() {
    return redirect_uri;
  }

  public void setRedirect_uri(final String redirect_uri) {
    this.redirect_uri = redirect_uri;
  }

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  public String getCode_challenge() {
    return code_challenge;
  }

  public void setCode_challenge(final String code_challenge) {
    this.code_challenge = code_challenge;
  }

  public String getCode_challenge_method() {
    return code_challenge_method;
  }

  public void setCode_challenge_method(final String code_challenge_method) {
    this.code_challenge_method = code_challenge_method;
  }
}
