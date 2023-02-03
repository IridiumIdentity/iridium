/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.email.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import software.iridium.api.base.domain.ApiResponse;

@ControllerAdvice
public class ApiExceptionHandler {

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public @ResponseBody ApiResponse handleValidationException(final Exception e) {
    return new ApiResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
  }
}
