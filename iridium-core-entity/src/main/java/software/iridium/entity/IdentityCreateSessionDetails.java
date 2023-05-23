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
package software.iridium.entity;

import jakarta.persistence.*;
import software.iridium.api.authentication.domain.CodeChallengeMethod;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "identity_create_session_details_id"))
@Table(name = "identity_create_session_details")
public class IdentityCreateSessionDetails extends AbstractEntity {

  @Column(name = "response_type", nullable = false, length = 50)
  private String responseType;

  @Column(name = "state", nullable = false, length = 255)
  private String state;

  @Column(name = "redirect_uri", nullable = false, length = 255)
  private String redirectUri;

  @Column(name = "code_challenge_method")
  @Enumerated(EnumType.STRING)
  private CodeChallengeMethod codeChallengeMethod;

  @Column(name = "code_challenge", nullable = false, length = 255)
  private String codeChallenge;

  @Column(name = "client_id", nullable = false, length = 255)
  private String clientId;

  @OneToOne(optional = false)
  @JoinColumn(name = "identity_id")
  private IdentityEntity identity;

  public String getResponseType() {
    return responseType;
  }

  public void setResponseType(final String responseType) {
    this.responseType = responseType;
  }

  public String getState() {
    return state;
  }

  public void setState(final String state) {
    this.state = state;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(final String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public CodeChallengeMethod getCodeChallengeMethod() {
    return codeChallengeMethod;
  }

  public void setCodeChallengeMethod(final CodeChallengeMethod codeChallengeMethod) {
    this.codeChallengeMethod = codeChallengeMethod;
  }

  public String getCodeChallenge() {
    return codeChallenge;
  }

  public void setCodeChallenge(final String codeChallenge) {
    this.codeChallenge = codeChallenge;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public IdentityEntity getIdentity() {
    return identity;
  }

  public void setIdentity(final IdentityEntity identity) {
    this.identity = identity;
  }
}
