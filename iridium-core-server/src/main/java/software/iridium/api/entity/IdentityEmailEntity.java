/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.entity;

import javax.persistence.*;

@Entity
@AttributeOverride(name="id", column=@Column(name="identity_email_address_id"))
@Table(name="identity_email_addresses",  uniqueConstraints={ @UniqueConstraint(columnNames ={ "identity_id", "email_address" })})
public class IdentityEmailEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = 2816177551193171742L;

    @Column(name = "email_address", unique = false)
    private String emailAddress;

    @Column(name = "verified")
    private Boolean verified = false;

    @Column(name = "primary")
    private Boolean primary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_id")
    private IdentityEntity identity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "email", orphanRemoval = true)
    private EmailVerificationTokenEntity emailVerificationToken;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean isVerified() {
        return verified;
    }

    public Boolean isNotVerified() {
        return !verified;
    }

    public void setVerified(final Boolean verified) {
        this.verified = verified;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public Boolean isPrimary() {
        return primary;
    }

    public void setPrimary(final Boolean primary) {
        this.primary = primary;
    }

    public IdentityEntity getIdentity() {
        return identity;
    }

    public void setIdentity(final IdentityEntity identity) {
        this.identity = identity;
    }

    public EmailVerificationTokenEntity getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(final EmailVerificationTokenEntity emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }
}
