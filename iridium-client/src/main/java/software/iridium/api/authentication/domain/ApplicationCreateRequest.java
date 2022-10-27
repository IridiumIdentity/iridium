/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationCreateRequest implements Serializable {

    private static final long serialVersionUID = -6208748801543357305L;

    public static final String MEDIA_TYPE = "application/vnd.iridium.id.authz.application-create-request.1+json";

    private String name;

    private String applicationTypeId;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getApplicationTypeId() {
        return applicationTypeId;
    }

    public void setApplicationTypeId(final String applicationTypeId) {
        this.applicationTypeId = applicationTypeId;
    }
}
