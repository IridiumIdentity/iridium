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
package software.iridium.api.base.domain;

public class ApiDataResponse<T> extends ApiResponse {

  private static final long serialVersionUID = -4047733646237443154L;
  private T data;

  public ApiDataResponse() {
    super("200", "OK");
    this.data = null;
  }

  public ApiDataResponse(T data) {
    super("200", "OK");
    this.data = data;
  }

  public ApiDataResponse(T data, String code, String message) {
    super(code, message);
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
