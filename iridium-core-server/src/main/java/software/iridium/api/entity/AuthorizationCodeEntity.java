/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.entity;

import software.iridium.api.authentication.domain.CodeChallengeMethod;

import javax.persistence.*;
import java.util.Date;

@Entity
@AttributeOverride(name="id", column=@Column(name="authorization_code_id"))
@Table(name="authorization_codes")
public class AuthorizationCodeEntity extends UuidIdentifiableAndAuditable {
    private static final long serialVersionUID = 8517063872625445676L;

    @Column(name = "redirect_uri", length = 255, nullable = true)
    private String redirectUri;

    @Column(name = "authorization_code", length = 32, nullable = false)
    private String authorizationCode;

    @Column(name = "client_id", length = 32,nullable = false)
    private String clientId;

    @Column(name = "identity_id", length = 36, nullable = false)
    private String identityId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expiration", nullable=false)
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
