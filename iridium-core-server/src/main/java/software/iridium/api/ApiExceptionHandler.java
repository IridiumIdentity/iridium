/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
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
