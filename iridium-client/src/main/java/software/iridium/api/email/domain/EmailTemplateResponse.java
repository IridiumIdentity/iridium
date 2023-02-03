/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.email.domain;

import java.io.Serializable;

public class EmailTemplateResponse implements Serializable {

  private static final long serialVersionUID = 6897368975283724735L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.email-template-response.1+json";

  private String id;

  private String content;

  private String filePath;

  private String tenantId;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public static String getMediaType() {
    return MEDIA_TYPE;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(final String filePath) {
    this.filePath = filePath;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }
}
