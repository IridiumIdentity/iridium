/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.entity;

import javax.persistence.*;
import software.iridium.api.authentication.domain.CodeChallengeMethod;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "identity_create_session_details_id"))
@Table(name = "identity_create_session_details")
public class IdentityCreateSessionDetails extends UuidIdentifiableAndAuditable {

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
