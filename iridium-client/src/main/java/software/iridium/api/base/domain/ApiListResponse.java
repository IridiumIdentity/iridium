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

import java.util.List;

public class ApiListResponse<T> extends ApiDataResponse<List<T>> {

  private static final long serialVersionUID = -1508644182734811250L;

  public ApiListResponse() {
    super();
  }

  public ApiListResponse(List<T> data) {
    super(data);
  }

  public ApiListResponse(List<T> data, String code, String message) {
    super(data, code, message);
  }
}
