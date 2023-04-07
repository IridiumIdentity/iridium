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

package software.iridium.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import software.iridium.api.base.domain.ApiResponse;
import software.iridium.api.base.error.*;

@ControllerAdvice
public class ApiExceptionHandler {

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public @ResponseBody ApiResponse handleValidationException(final Exception e) {
    return new ApiResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
  }

  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(NotAuthorizedException.class)
  public @ResponseBody ApiResponse handleNotAuthorizedException(final Exception e) {
    return new ApiResponse(HttpStatus.UNAUTHORIZED.toString(), e.getMessage());
  }

  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ExceptionHandler(DuplicateResourceException.class)
  public @ResponseBody ApiResponse handeDuplicateResourceException(final Exception e) {
    return new ApiResponse(HttpStatus.CONFLICT.toString(), e.getMessage());
  }

  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public @ResponseBody ApiResponse handleResourceNotFoundException(final Exception e) {
    return new ApiResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage());
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequestException.class)
  public @ResponseBody AccessTokenErrorResponse handleAccessTokenBadRequest(final Exception e) {
    return new AccessTokenErrorResponse(
        HttpStatus.BAD_REQUEST.toString(), e.getMessage().toLowerCase());
  }
}
