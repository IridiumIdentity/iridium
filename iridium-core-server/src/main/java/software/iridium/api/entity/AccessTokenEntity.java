/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@AttributeOverride(name="id", column=@Column(name="access_token_id"))
@Table(name="access_tokens")
public class AccessTokenEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = 4386713626058738418L;

    @Column(name = "access_token", nullable = false, updatable = false, length = 100)
    private String accessToken;

    @Column(name = "type", nullable = false, updatable = false, length = 25)
    private String tokenType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration", nullable = false)
    private Date expiration;

    @Column(name = "identity_id", length = 36, nullable = false)
    private String identityId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refresh_token_id")
    private RefreshTokenEntity refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(final String type) {
        this.tokenType = type;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(final Date expiration) {
        this.expiration = expiration;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(final String identityId) {
        this.identityId = identityId;
    }

    public RefreshTokenEntity getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final RefreshTokenEntity refreshToken) {
        this.refreshToken = refreshToken;
    }
}
