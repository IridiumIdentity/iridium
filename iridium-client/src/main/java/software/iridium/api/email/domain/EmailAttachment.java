/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
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
