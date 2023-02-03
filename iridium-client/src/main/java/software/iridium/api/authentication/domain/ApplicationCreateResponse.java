/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationCreateResponse implements Serializable {

  private static final long serialVersionUID = -7816794958442604679L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.authz.application-create-response.1+json";

  private String id;

  private String name;

  private String applicationTypeId;

  private String tenantId;

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

  public String getApplicationTypeId() {
    return applicationTypeId;
  }

  public void setApplicationTypeId(final String applicationTypeId) {
    this.applicationTypeId = applicationTypeId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }
}
