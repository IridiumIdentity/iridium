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
package software.iridium.api.entity;

import java.util.Date;
import javax.persistence.*;
import software.iridium.api.authentication.domain.CodeChallengeMethod;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "authorization_code_id"))
@Table(name = "authorization_codes")
public class AuthorizationCodeEntity extends UuidIdentifiableAndAuditable {
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

  public String getRedirectUri() {
    return redirectUri;
  }

  public void setRedirectUri(final String redirectUri) {
    this.redirectUri = redirectUri;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public String getIdentityId() {
    return identityId;
  }

  public void setIdentityId(final String identityId) {
    this.identityId = identityId;
  }

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(final Date expiration) {
    this.expiration = expiration;
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

  public String getAuthorizationCode() {
    return authorizationCode;
  }

  public void setAuthorizationCode(final String authorizationCode) {
    this.authorizationCode = authorizationCode;
  }
}
