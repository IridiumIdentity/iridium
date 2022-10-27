/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */
package software.iridium.api.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AttributeOverride(name="id", column=@Column(name="identity_id"))
@Table(name="identities")
public class IdentityEntity extends UuidIdentifiableAndAuditable {

    private static final long serialVersionUID = -649520696707218781L;

    @Column(name="failed_login_attempts", nullable=false)
    private Integer failedLoginAttempts = 0;

    @Column(name="encoded_password", length=255, nullable=true)
    private String encodedPassword;

    @Column(name="external_id", length=255, nullable=true)
    private String externalId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_successful_login", nullable=true)
    private Date lastSuccessfulLogin;

    @Column(name="locked", nullable=false)
    private Boolean locked = false;

    @Column(name = "requires_password_change", nullable = false)
    private Boolean requiresPasswordChange = false;

    @ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="tenants_identities_xref", joinColumns= {@JoinColumn(name="identity_id")},
            inverseJoinColumns= {@JoinColumn(name="tenant_id")})
    private List<TenantEntity> managedTenants = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "identities_applications",
            joinColumns = @JoinColumn(name = "identity_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private List<ApplicationEntity> authorizedApplications = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "create_session_details_id")
    private IdentityCreateSessionDetails createSessionDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "password_reset_token_id")
    private PasswordResetTokenEntity passwordResetToken;

    @ManyToOne(optional = true)
    @JoinColumn(name = "provider_id")
    private ExternalIdentityProviderEntity provider;

    @Column(name = "parent_tenant_id", length = 36, nullable = false)
    private String parentTenantId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "identity", orphanRemoval = true)
    private List<IdentityPropertyEntity> identityProperties = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "identity", orphanRemoval = true)
    private List<IdentityEmailEntity> emails = new ArrayList<>();

    @OneToMany(mappedBy = "identity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScopeEntity> scopes = new ArrayList<>();

    @ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="roles_identities_xref", joinColumns= {@JoinColumn(name="identity_id")},
            inverseJoinColumns= {@JoinColumn(name="role_id")})
    private Set<RoleEntity> roles = new HashSet<>();

    public List<IdentityEmailEntity> getEmails() {
        return emails;
    }

    public void setEmails(final List<IdentityEmailEntity> emails) {
        this.emails = emails;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(final Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Date getLastSuccessfulLogin() {
        return lastSuccessfulLogin;
    }

    public void setLastSuccessfulLogin(final Date lastSuccessfulLogin) {
        this.lastSuccessfulLogin = lastSuccessfulLogin;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isNotLocked() {
        return !isLocked();
    }

    public void setLocked(final Boolean locked) {
        this.locked = locked;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(final Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public ProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(final ProfileEntity profile) {
        this.profile = profile;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(final String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public Boolean doesNotRequirePasswordChange() {
        return !requiresPasswordChange;
    }

    public Boolean doesRequiresPasswordChange() {
        return requiresPasswordChange;
    }

    public void setRequiresPasswordChange(final Boolean requiresPasswordChange) {
        this.requiresPasswordChange = requiresPasswordChange;
    }

    public PasswordResetTokenEntity getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(final PasswordResetTokenEntity passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public ExternalIdentityProviderEntity getProvider() {
        return provider;
    }

    public void setProvider(final ExternalIdentityProviderEntity provider) {
        this.provider = provider;
    }

    public List<IdentityPropertyEntity> getIdentityProperties() {
        return identityProperties;
    }

    public void setIdentityProperties(final List<IdentityPropertyEntity> identityProperties) {
        this.identityProperties = identityProperties;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(final String externalId) {
        this.externalId = externalId;
    }

    public boolean isExternalUser() {
        return this.externalId != null;
    }

    @Transient
    public IdentityEmailEntity getPrimaryEmail() {
        for (IdentityEmailEntity email : getEmails()) {
            if (email.isPrimary()) {
                return email;
            }
        }
        return null;
    }

    public List<TenantEntity> getManagedTenants() {
        return managedTenants;
    }

    public void setManagedTenants(final List<TenantEntity> tenants) {
        this.managedTenants = tenants;
    }

    public String getParentTenantId() {
        return parentTenantId;
    }

    public void setParentTenantId(final String tenantId) {
        this.parentTenantId = tenantId;
    }

    public List<ScopeEntity> getScopes() {
        return scopes;
    }

    public void setScopes(final List<ScopeEntity> scopes) {
        this.scopes = scopes;
    }

    public List<ApplicationEntity> getAuthorizedApplications() {
        return authorizedApplications;
    }

    public void setAuthorizedApplications(final List<ApplicationEntity> authorizedApplications) {
        this.authorizedApplications = authorizedApplications;
    }

    public IdentityCreateSessionDetails getCreateSessionDetails() {
        return createSessionDetails;
    }

    public void setCreateSessionDetails(final IdentityCreateSessionDetails createSessionDetails) {
        this.createSessionDetails = createSessionDetails;
    }
}
