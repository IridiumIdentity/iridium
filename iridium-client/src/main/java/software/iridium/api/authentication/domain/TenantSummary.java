/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class TenantSummary implements Serializable {

  public static final String MEDIA_TYPE_LIST =
      "application/vnd.iridium.id.tenant-summary-list.1+json";

  private String id;

  private String subdomain;

  private TenantSummary(final String id, final String subdomain) {
    this.id = id;
    this.subdomain = subdomain;
  }

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

  public static TenantSummary of(final String id, final String name) {
    return new TenantSummary(id, name);
  }
}
