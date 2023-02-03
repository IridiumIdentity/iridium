/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ProviderSummaryResponse implements Serializable {
  private static final long serialVersionUID = -9112248887244862882L;

  public static final String MEDIA_TYPE_LIST =
      "application/vnd.iridium.id.provider-summary-list.1+json";

  private String id;

  private String name;

  private String iconPath;

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

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(final String iconPath) {
    this.iconPath = iconPath;
  }
}
