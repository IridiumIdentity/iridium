/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.entity;

import software.iridium.api.authentication.domain.Environment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AttributeOverride(name="id", column=@Column(name="tenant_id"))
@Table(name="tenants")
public class TenantEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = 689089741127267195L;

    @Column(name="subdomain", length=100, nullable=false, unique=true, updatable=false)
    private String subdomain;

    @ManyToMany(mappedBy = "managedTenants", fetch = FetchType.LAZY)
    private List<IdentityEntity> managingIdentities = new ArrayList<>();

    @Column(name="website_url", length=255, nullable=false, unique=true, updatable=true)
    private String websiteUrl;

    @Column(name = "environment")
    @Enumerated(EnumType.STRING)
    private Environment environment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "login_descriptor_id")
    private LoginDescriptorEntity loginDescriptor;

    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY)
    private List<ExternalIdentityProviderEntity> externalIdentityProviders = new ArrayList<>();

    public List<IdentityEntity> getManagingIdentities() {
        return managingIdentities;
    }

    public void setManagingIdentities(final List<IdentityEntity> identities) {
        this.managingIdentities = identities;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(final String subdomain) {
        this.subdomain = subdomain;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    public LoginDescriptorEntity getLoginDescriptor() {
        return loginDescriptor;
    }

    public void setLoginDescriptor(final LoginDescriptorEntity loginDescriptor) {
        this.loginDescriptor = loginDescriptor;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(final String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public List<ExternalIdentityProviderEntity> getExternalIdentityProviders() {
        return externalIdentityProviders;
    }

    public void setExternalIdentityProviders(final List<ExternalIdentityProviderEntity> externalIdentityProviders) {
        this.externalIdentityProviders = externalIdentityProviders;
    }
}
