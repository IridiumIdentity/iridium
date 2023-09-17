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

public class ExternalIdentityProviderUpdateResponse implements Serializable {

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.external-provider-update-response.1+json";
  @Serial private static final long serialVersionUID = -7703169295651443152L;

  private String id;

  private String clientId;

  private ExternalIdentityProviderUpdateResponse(final String id, final String clientId) {
    this.id = id;
    this.clientId = clientId;
  }

  public String getId() {
    return id;
  }

  public String getClientId() {
    return clientId;
  }

  public static ExternalIdentityProviderUpdateResponse of(final String id, final String clientId) {
    return new ExternalIdentityProviderUpdateResponse(id, clientId);
  }

  @Override
  public String toString() {
    return "ExternalIdentityProviderUpdateResponse{"
        + "id='"
        + id
        + '\''
        + ", clientId='"
        + clientId
        + '\''
        + '}';
  }
}
