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

public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = 3601878465892523213L;

  private static final String CODE = "403";
  private static final String MESSAGE = "FORBIDDEN";

  public AccessDeniedException() {
    super(MESSAGE);
  }

  public AccessDeniedException(final String message) {
    super(message);
  }

  public String getCode() {
    return CODE;
  }
}
