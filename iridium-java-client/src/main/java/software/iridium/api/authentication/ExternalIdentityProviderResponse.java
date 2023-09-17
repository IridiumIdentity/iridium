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
package software.iridium.api.authentication;

import java.io.Serial;
import java.io.Serializable;

public class ExternalIdentityProviderResponse implements Serializable {

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.external-provider-response.1+json";
  @Serial private static final long serialVersionUID = -3896288291572117748L;

  private String id;

  private String clientId;

  private String name;

  private String clientSecret;

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
  }

  @Override
  public String toString() {
    return "ExternalIdentityProviderResponse{"
        + "id='"
        + id
        + '\''
        + ", clientId='"
        + clientId
        + '\''
        + ", name='"
        + name
        + '\''
        + '}';
  }
}
