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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "client_secret_id"))
@Table(name = "client_secrets")
public class ClientSecretEntity extends AbstractEntity {

  private static final long serialVersionUID = -2768574776374607276L;

  @Column(name = "secret_key", updatable = false, nullable = false)
  private String secretKey;

  @ManyToOne
  @JoinColumn(name = "application_id")
  private ApplicationEntity application;

  /**
   * public String getSecretKey() { return secretKey; }
   *
   * <p>public void setSecretKey(final String secretKey) { this.secretKey = secretKey; }
   *
   * <p>public ApplicationEntity getApplication() { return application; }
   *
   * <p>public void setApplication(final ApplicationEntity application) { this.application =
   * application; }
   */
}
