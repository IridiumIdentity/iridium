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
package software.iridium.entity;

import jakarta.persistence.*;
import java.io.Serial;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "refresh_token_id"))
@Table(name = "refresh_tokens")
public class RefreshTokenEntity extends AbstractEntity {

  @Serial private static final long serialVersionUID = 474966926438091101L;

  @Column(name = "refresh_token", nullable = false, updatable = false, length = 100)
  private String refreshToken;

  @OneToOne(mappedBy = "refreshToken", optional = false)
  private AccessTokenEntity accessToken;

  /**
   * public String getRefreshToken() { return refreshToken; }
   *
   * <p>public void setRefreshToken(final String refreshToken) { this.refreshToken = refreshToken; }
   *
   * <p>public AccessTokenEntity getAccessToken() { return accessToken; }
   *
   * <p>public void setAccessToken(final AccessTokenEntity accessToken) { this.accessToken =
   * accessToken; }
   */
}
