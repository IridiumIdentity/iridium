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
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.iridium.api.authentication.domain.CodeChallengeMethod;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "authorization_code_id"))
@Table(name = "authorization_codes")
public class AuthorizationCodeEntity extends AbstractEntity {
  private static final long serialVersionUID = 8517063872625445676L;

  @Column(name = "redirect_uri", length = 255, nullable = true)
  private String redirectUri;

  @Column(name = "authorization_code", length = 32, nullable = false)
  private String authorizationCode;

  @Column(name = "client_id", length = 32, nullable = false)
  private String clientId;

  @Column(name = "identity_id", length = 36, nullable = false)
  private String identityId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "expiration", nullable = false)
  private Date expiration;

  @Column(name = "code_challenge_method")
  @Enumerated(EnumType.STRING)
  private CodeChallengeMethod codeChallengeMethod;

  @Column(name = "code_challenge", length = 255, nullable = true)
  private String codeChallenge;

  /**
   * public String getRedirectUri() { return redirectUri; }
   *
   * <p>public void setRedirectUri(final String redirectUri) { this.redirectUri = redirectUri; }
   *
   * <p>public String getClientId() { return clientId; }
   *
   * <p>public void setClientId(final String clientId) { this.clientId = clientId; }
   *
   * <p>public String getIdentityId() { return identityId; }
   *
   * <p>public void setIdentityId(final String identityId) { this.identityId = identityId; }
   *
   * <p>public Date getExpiration() { return expiration; }
   *
   * <p>public void setExpiration(final Date expiration) { this.expiration = expiration; }
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
   * <p>public String getAuthorizationCode() { return authorizationCode; }
   *
   * <p>public void setAuthorizationCode(final String authorizationCode) { this.authorizationCode =
   * authorizationCode; }
   */
}
