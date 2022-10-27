/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.entity;

import javax.persistence.*;

@Entity
@AttributeOverride(name="id", column=@Column(name="refresh_token_id"))
@Table(name="refresh_tokens")
public class RefreshTokenEntity extends UuidIdentifiableAndAuditable {

    @Column(name = "refresh_token", nullable = false, updatable = false, length = 100)
    private String refreshToken;

    @OneToOne(mappedBy = "refreshToken", optional = false)
    private AccessTokenEntity accessToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public AccessTokenEntity getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final AccessTokenEntity accessToken) {
        this.accessToken = accessToken;
    }
}
