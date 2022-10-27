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
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@AttributeOverride(name="id", column=@Column(name="group_id"))
@Table(name="roles")
public class RoleEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = 3782334153369269308L;

    @Column(name = "name", length = 128, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<IdentityEntity> identities = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<IdentityEntity> getIdentities() {
        return identities;
    }

    public void setIdentities(final Set<IdentityEntity> identities) {
        this.identities = identities;
    }
}
