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

public class CreateExternalIdentityProviderResponse implements Serializable {

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.external-identity-provider-create-response.1+json";

  @Serial private static final long serialVersionUID = -2123644796168788095L;

  private CreateExternalIdentityProviderResponse(final String id, final String name) {
    this.id = id;
    this.name = name;
  }

  private String id;

  private String name;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public static CreateExternalIdentityProviderResponse of(final String id, final String name) {
    return new CreateExternalIdentityProviderResponse(id, name);
  }

  @Override
  public String toString() {
    return "CreateExternalIdentityProviderResponse{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + '}';
  }
}
