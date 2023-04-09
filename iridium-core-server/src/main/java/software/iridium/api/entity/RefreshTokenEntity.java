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

package software.iridium.api.entity;

import jakarta.persistence.*;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "refresh_token_id"))
@Table(name = "refresh_tokens")
public class RefreshTokenEntity extends AbstractEntity {

  @Column(name = "refresh_token", nullable = false, updatable = false, length = 100)
  private String refreshToken;

  @OneToOne(mappedBy = "refreshToken", optional = false)
  private AccessTokenEntity accessToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(final String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public AccessTokenEntity getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(final AccessTokenEntity accessToken) {
    this.accessToken = accessToken;
  }
}
