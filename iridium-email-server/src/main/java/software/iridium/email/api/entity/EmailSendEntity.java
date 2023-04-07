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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import software.iridium.api.entity.UuidIdentifiableAndAuditable;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "email_send_id"))
@Table(name = "email_sends")
public class EmailSendEntity extends UuidIdentifiableAndAuditable {

  private static final long serialVersionUID = 5369868319213866813L;

  @Column(name = "tenant_id", nullable = false, length = 36)
  private String tenantId;

  @Column(name = "email_template_id", nullable = false, length = 36)
  private String emailTemplateId;

  @Column(name = "body", nullable = false, length = 512)
  private String body;

  @Column(name = "to", nullable = false, length = 100)
  private String to;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "email", fetch = FetchType.LAZY)
  private List<EmailAttachmentEntity> attachments = new ArrayList<>();

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }

  public String getEmailTemplateId() {
    return emailTemplateId;
  }

  public void setEmailTemplateId(final String emailTemplateId) {
    this.emailTemplateId = emailTemplateId;
  }

  public String getBody() {
    return body;
  }

  public void setBody(final String body) {
    this.body = body;
  }

  public String getTo() {
    return to;
  }

  public void setTo(final String to) {
    this.to = to;
  }

  public List<EmailAttachmentEntity> getAttachments() {
    return attachments;
  }

  public void setAttachments(final List<EmailAttachmentEntity> attachments) {
    this.attachments = attachments;
  }
}
