/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class CreateTenantResponse implements Serializable {
    private static final long serialVersionUID = 2934423305284268147L;
    public static final String MEDIA_TYPE = "application/vnd.iridium.id.authn.tenant-create-response1+json";

    private String id;

    private String subdomain;

    private Environment environment;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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
}
