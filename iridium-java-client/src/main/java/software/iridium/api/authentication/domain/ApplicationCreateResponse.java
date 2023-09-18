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
package software.iridium.api.authentication.domain;

import java.io.Serializable;

public class ApplicationCreateResponse implements Serializable {

  private static final long serialVersionUID = -7816794958442604679L;

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.application-create-response.1+json";

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

  @Override
  public String toString() {
    return "ApplicationCreateResponse{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", applicationTypeId='"
        + applicationTypeId
        + '\''
        + ", tenantId='"
        + tenantId
        + '\''
        + '}';
  }
}
