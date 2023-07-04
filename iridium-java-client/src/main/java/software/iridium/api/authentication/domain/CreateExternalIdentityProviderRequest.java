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

public class CreateExternalIdentityProviderRequest implements Serializable {

  public static final String MEDIA_TYPE =
      "application/vnd.iridium.id.external-identity-provider-create-request.1+json";
  @Serial private static final long serialVersionUID = 4206566507180153217L;

  private String externalProviderTemplateId;

  private String clientId;

  private String clientSecret;

  public String getExternalProviderTemplateId() {
    return externalProviderTemplateId;
  }

  public void setExternalProviderTemplateId(final String externalProviderTemplateId) {
    this.externalProviderTemplateId = externalProviderTemplateId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(final String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
