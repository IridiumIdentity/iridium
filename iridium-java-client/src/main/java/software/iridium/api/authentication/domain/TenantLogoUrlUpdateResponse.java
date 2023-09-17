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

import java.io.Serial;
import java.io.Serializable;

public class TenantLogoUrlUpdateResponse implements Serializable {

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.tenant-logo-update-response.1+json";
  @Serial private static final long serialVersionUID = -5720504031836694446L;

  private String tenantId;
  private String loginDescriptorId;
  private String logoUrl;

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(final String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }

  public String getLoginDescriptorId() {
    return loginDescriptorId;
  }

  public void setLoginDescriptorId(final String loginDescriptorId) {
    this.loginDescriptorId = loginDescriptorId;
  }

  @Override
  public String toString() {
    return "TenantLogoUrlUpdateResponse{"
        + "tenantId='"
        + tenantId
        + '\''
        + ", loginDescriptorId='"
        + loginDescriptorId
        + '\''
        + ", logoUrl='"
        + logoUrl
        + '\''
        + '}';
  }
}
