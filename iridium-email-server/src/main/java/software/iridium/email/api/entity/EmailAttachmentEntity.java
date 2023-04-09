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

package software.iridium.email.api.entity;

import jakarta.persistence.*;
import software.iridium.api.entity.AbstractEntity;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "email_attachment_id"))
@Table(name = "email_attachment")
public class EmailAttachmentEntity extends AbstractEntity {

  private static final long serialVersionUID = 5881323464921939514L;

  @Column(name = "file_path", length = 255, nullable = false)
  private String filePath;

  @Column(name = "attachment_name", length = 100, nullable = false)
  private String attachmentName;

  @Column(name = "tenant_id", length = 36, nullable = false)
  private String tenantId;

  @ManyToOne
  @JoinColumn(name = "email_entity_id")
  private EmailSendEntity email;

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(final String filePath) {
    this.filePath = filePath;
  }

  public String getAttachmentName() {
    return attachmentName;
  }

  public void setAttachmentName(final String attachmentName) {
    this.attachmentName = attachmentName;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }

  public EmailSendEntity getEmail() {
    return email;
  }

  public void setEmail(final EmailSendEntity email) {
    this.email = email;
  }
}
