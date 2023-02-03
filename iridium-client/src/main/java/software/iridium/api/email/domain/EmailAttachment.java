/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.email.domain;

import java.io.Serializable;

public class EmailAttachment implements Serializable {

  private static final long serialVersionUID = -6177710152362088025L;
  private String attachmentFileName;

  private String fileId;

  public String getAttachmentFileName() {
    return attachmentFileName;
  }

  public void setAttachmentFileName(final String attachmentFileName) {
    this.attachmentFileName = attachmentFileName;
  }

  public String getFileId() {
    return fileId;
  }

  public void setFileId(final String fileId) {
    this.fileId = fileId;
  }
}
