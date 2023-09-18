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

public class ExternalProviderTemplateSummaryResponse implements Serializable {
  private static final long serialVersionUID = -9112248887244862882L;

  public static final String MEDIA_TYPE_LIST =
      "application/vnd.iridium.id.external-provider-template-summary-list.1+json";

  private String id;

  private String name;

  private String iconPath;

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

  public String getIconPath() {
    return iconPath;
  }

  public void setIconPath(final String iconPath) {
    this.iconPath = iconPath;
  }

  @Override
  public String toString() {
    return "ExternalProviderTemplateSummaryResponse{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", iconPath='"
        + iconPath
        + '\''
        + '}';
  }
}
