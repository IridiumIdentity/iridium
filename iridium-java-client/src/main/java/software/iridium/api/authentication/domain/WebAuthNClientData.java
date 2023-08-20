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

public class WebAuthNClientData implements Serializable {

  @Serial private static final long serialVersionUID = -2622839440542261083L;
  private String type;

  private String challenge;

  private String origin;

  private String crossOrigin;

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getChallenge() {
    return challenge;
  }

  public void setChallenge(final String challenge) {
    this.challenge = challenge;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(final String origin) {
    this.origin = origin;
  }

  public String getCrossOrigin() {
    return crossOrigin;
  }

  public void setCrossOrigin(final String crossOrigin) {
    this.crossOrigin = crossOrigin;
  }
}
