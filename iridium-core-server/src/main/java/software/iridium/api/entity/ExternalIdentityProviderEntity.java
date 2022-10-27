/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AttributeOverride(name="id", column=@Column(name="external_identity_provider_id"))
@Table(name="external_identity_providers")
public class ExternalIdentityProviderEntity extends UuidIdentifiableAndAuditable {
    private static final long serialVersionUID = 107595733607149226L;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "client_id", length = 255, nullable = false)
    private String clientId;

    @Column(name = "client_secret", length = 255, nullable = true)
    private String clientSecret;

    @ManyToOne(optional = false)
    @JoinColumn(name = "external_identity_provider_template_id")
    private ExternalIdentityProviderTemplateEntity template;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tenant_id")
    private TenantEntity tenant;

    @Column(name = "icon_path", length = 255, nullable = false)
    private String iconPath;

    @Column(name = "redirect_uri", length = 255, nullable = false)
    private String redirectUri;

    @Column(name = "base_authorization_url", length = 2355, nullable = false)
    private String baseAuthorizationUrl;

    @Column(name = "scope", length = 255, nullable = true)
    private String scope;

    @Column(name = "access_token_request_base_url", length = 255, nullable = false)
    private String accessTokenRequestBaseUrl;

    @Column(name = "profile_request_base_url", length = 255, nullable = false)
    private String profileRequestBaseUrl;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "provider", orphanRemoval = true)
    private List<ExternalIdentityProviderPropertyEntity> properties = new ArrayList<>();

    private Boolean syncAttributesOnEachLogin;

    public ExternalIdentityProviderTemplateEntity getTemplate() {
        return template;
    }

    public void setTemplate(final ExternalIdentityProviderTemplateEntity template) {
        this.template = template;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public void setTenant(final TenantEntity tenant) {
        this.tenant = tenant;
    }

    public List<ExternalIdentityProviderPropertyEntity> getProperties() {
        return properties;
    }

    public void setProperties(final List<ExternalIdentityProviderPropertyEntity> properties) {
        this.properties = properties;
    }

    public Boolean getSyncAttributesOnEachLogin() {
        return syncAttributesOnEachLogin;
    }

    public void setSyncAttributesOnEachLogin(final Boolean syncAttributesOnEachLogin) {
        this.syncAttributesOnEachLogin = syncAttributesOnEachLogin;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(final String redirectUrl) {
        this.redirectUri = redirectUrl;
    }

    public String getAccessTokenRequestBaseUrl() {
        return accessTokenRequestBaseUrl;
    }

    public void setAccessTokenRequestBaseUrl(final String identityRequestBaseUrl) {
        this.accessTokenRequestBaseUrl = identityRequestBaseUrl;
    }

    public String getProfileRequestBaseUrl() {
        return profileRequestBaseUrl;
    }

    public void setProfileRequestBaseUrl(final String profileRequestBaseUrl) {
        this.profileRequestBaseUrl = profileRequestBaseUrl;
    }

    public String getBaseAuthorizationUrl() {
        return baseAuthorizationUrl;
    }

    public void setBaseAuthorizationUrl(final String baseAuthorizationUrl) {
        this.baseAuthorizationUrl = baseAuthorizationUrl;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(final String scope) {
        this.scope = scope;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(final String iconPath) {
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
