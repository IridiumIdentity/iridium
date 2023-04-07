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

public class TenantSummary implements Serializable {

  public static final String MEDIA_TYPE_LIST =
      "application/vnd.iridium.id.tenant-summary-list.1+json";

  private String id;

  private String subdomain;

  private TenantSummary(final String id, final String subdomain) {
    this.id = id;
    this.subdomain = subdomain;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getSubdomain() {
    return subdomain;
  }

  public void setSubdomain(final String subdomain) {
    this.subdomain = subdomain;
  }

  public static TenantSummary of(final String id, final String name) {
    return new TenantSummary(id, name);
  }
}
