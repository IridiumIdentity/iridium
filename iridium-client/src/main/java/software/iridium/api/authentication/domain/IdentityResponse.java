/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IdentityResponse implements Serializable {

	public static final String MEDIA_TYPE = "application/vnd.iridium.id.identity-response.1+json";

	private static final long serialVersionUID = 7221668651862331716L;

	private String id;

	private String username;

	private ProfileResponse profile;

	@JsonIgnore
	private String appBaseUrl;

	@JsonIgnore
	private String applicationName;

	@JsonIgnore
	private String tenantWebsite;

	private String userToken;

	private Set<String> roles = new HashSet<>();

	private List<String> tenantIds = new ArrayList<>();

	public String getUsername() {
		return username;
	}
	
	public void setUsername(final String username) {
		this.username = username;
	}
	
	public Set<String> getRoles() {
		return roles;
	}
	
	public void setRoles(final Set<String> roles) {
		this.roles = roles;
	}

	public String getId() {
		return id;
	}
	
	public void setId(final String id) {
		this.id = id;
	}

	public ProfileResponse getProfile() {
		return profile;
	}

	public void setProfile(final ProfileResponse profile) {
		this.profile = profile;
	}

	public List<String> getTenantIds() {
		return tenantIds;
	}

	public void setTenantIds(final List<String> tenantIds) {
		this.tenantIds = tenantIds;
	}

	public String getAppBaseUrl() {
		return appBaseUrl;
	}

	public void setAppBaseUrl(final String appBaseUrl) {
		this.appBaseUrl = appBaseUrl;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(final String applicationName) {
		this.applicationName = applicationName;
	}

	public String getTenantWebsite() {
		return tenantWebsite;
	}

	public void setTenantWebsite(final String tenantWebsite) {
		this.tenantWebsite = tenantWebsite;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(final String userToken) {
		this.userToken = userToken;
	}
}
