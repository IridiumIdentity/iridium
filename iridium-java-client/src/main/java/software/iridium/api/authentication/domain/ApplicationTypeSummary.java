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

public class ApplicationTypeSummary implements Serializable {
  private static final long serialVersionUID = -2596785945095858657L;

  public static final String MEDIA_TYPE_LIST =
      "application/vnd.iridium.id.application-type-summary-list.1+json";

  private String id;

  private String name;

  private String description;

  private ApplicationTypeSummary(final String id, final String name, final String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public static ApplicationTypeSummary of(
      final String id, final String name, final String description) {
    return new ApplicationTypeSummary(id, name, description);
  }

  @Override
  public String toString() {
    return "ApplicationTypeSummary{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
