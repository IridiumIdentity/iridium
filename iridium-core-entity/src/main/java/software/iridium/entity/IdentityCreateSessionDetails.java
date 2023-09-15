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
import java.io.Serial;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.iridium.api.authentication.domain.CodeChallengeMethod;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "identity_create_session_details_id"))
@Table(name = "identity_create_session_details")
public class IdentityCreateSessionDetails extends AbstractEntity {

  @Serial private static final long serialVersionUID = -2979796008615001766L;

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

  /**
   * public String getResponseType() { return responseType; }
   *
   * <p>public void setResponseType(final String responseType) { this.responseType = responseType; }
   *
   * <p>public String getState() { return state; }
   *
   * <p>public void setState(final String state) { this.state = state; }
   *
   * <p>public String getRedirectUri() { return redirectUri; }
   *
   * <p>public void setRedirectUri(final String redirectUri) { this.redirectUri = redirectUri; }
   *
   * <p>public CodeChallengeMethod getCodeChallengeMethod() { return codeChallengeMethod; }
   *
   * <p>public void setCodeChallengeMethod(final CodeChallengeMethod codeChallengeMethod) {
   * this.codeChallengeMethod = codeChallengeMethod; }
   *
   * <p>public String getCodeChallenge() { return codeChallenge; }
   *
   * <p>public void setCodeChallenge(final String codeChallenge) { this.codeChallenge =
   * codeChallenge; }
   *
   * <p>public String getClientId() { return clientId; }
   *
   * <p>public void setClientId(final String clientId) { this.clientId = clientId; }
   *
   * <p>public IdentityEntity getIdentity() { return identity; }
   *
   * <p>public void setIdentity(final IdentityEntity identity) { this.identity = identity; }
   */
}
