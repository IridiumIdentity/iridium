/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationTypeSummary implements Serializable {
  private static final long serialVersionUID = -2596785945095858657L;

  public static final String MEDIA_TYPE_LIST =
      "application/vnd.iridium.id.authz.application-type-summary-list.1+json";

  private String id;

  private String name;

  private String description;

  private ApplicationTypeSummary(final String id, final String name, final String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public static ApplicationTypeSummary of(
      final String id, final String name, final String description) {
    return new ApplicationTypeSummary(id, name, description);
  }
}
