/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@AttributeOverride(name="id", column=@Column(name="profile_id"))
@Table(name="profiles")
public class ProfileEntity extends UuidIdentifiableAndAuditable {

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @OneToOne(mappedBy = "profile", optional = false)
    private IdentityEntity identity;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public IdentityEntity getIdentity() {
        return identity;
    }

    public void setIdentity(final IdentityEntity identity) {
        this.identity = identity;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
}
