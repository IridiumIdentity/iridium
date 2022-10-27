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
    public @ResponseBody
    ApiResponse handleValidationException(final Exception e) {
        return new ApiResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
    }
}
