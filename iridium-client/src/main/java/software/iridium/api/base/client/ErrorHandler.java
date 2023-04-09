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

package software.iridium.api.base.client;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import software.iridium.api.base.error.*;

public class ErrorHandler {

  public void handleErrors(final HttpStatusCode httpStatusCode) {
    final var httpStatus = HttpStatus.resolve(httpStatusCode.value());

    switch (httpStatus) {
      case UNAUTHORIZED -> throw new NotAuthorizedException(httpStatus.getReasonPhrase());
      case FORBIDDEN -> throw new AccessDeniedException(httpStatus.getReasonPhrase());
      case NOT_FOUND -> throw new ResourceNotFoundException(httpStatus.getReasonPhrase());
      case CONFLICT -> throw new DuplicateResourceException(httpStatus.getReasonPhrase());
      case INTERNAL_SERVER_ERROR -> throw new ClientCallException(httpStatus.getReasonPhrase());
      default -> throw new ClientCallException(
          "received "
              + httpStatus.value()
              + " response with reason "
              + httpStatus.getReasonPhrase());
    }
  }
}
