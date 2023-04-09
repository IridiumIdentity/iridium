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

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import software.iridium.api.entity.AbstractEntity;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "email_template_id"))
@Table(name = "email_templates")
public class EmailTemplateEntity extends AbstractEntity {

  private static final long serialVersionUID = 7103996733194711078L;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "tenant_id", nullable = false, length = 36)
  private String tenantId;

  @Column(name = "file_path", nullable = false, length = 255)
  private String filePath;

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(final String filePath) {
    this.filePath = filePath;
  }
}
