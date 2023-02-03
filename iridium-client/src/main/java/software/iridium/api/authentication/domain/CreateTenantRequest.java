/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class CreateTenantRequest implements Serializable {

  private static final long serialVersionUID = -3894079971320301359L;
  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.authn.tenant-create-request.1+json";

  private String subdomain;

  private Environment environment;

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
