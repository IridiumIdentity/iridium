/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2022.
 */

package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationSummary implements Serializable {

    private static final long serialVersionUID = 5850112593764351364L;

    public static final String MEDIA_TYPE_LIST = "application/vnd.iridium.id.application-summary-list.1+json";

    private String id;

    private String name;

    private String iconUrl;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(final String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
