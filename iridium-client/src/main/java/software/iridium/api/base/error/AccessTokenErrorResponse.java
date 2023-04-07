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

package software.iridium.api.base.error;

import java.io.Serializable;

public class AccessTokenErrorResponse implements Serializable {

  private static final long serialVersionUID = 1524799629250981083L;
  private String code;
  private String message;

  public AccessTokenErrorResponse() {
    super();
    this.code = "";
    this.message = "";
  }

  public AccessTokenErrorResponse(final String code, final String message) {
    this();
    this.setCode(code);
    this.setMessage(message);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String messages) {
    this.message = messages;
  }
}
