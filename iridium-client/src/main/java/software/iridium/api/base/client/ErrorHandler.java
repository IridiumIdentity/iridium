package software.iridium.api.base.client;

import org.springframework.http.HttpStatus;
import software.iridium.api.base.error.*;

public class ErrorHandler {

    public void handleErrors(final HttpStatus httpStatus) {

        switch (httpStatus) {
            case UNAUTHORIZED:
                throw new NotAuthorizedException(httpStatus.getReasonPhrase());
            case FORBIDDEN:
                throw new AccessDeniedException(httpStatus.getReasonPhrase());
            case NOT_FOUND:
                throw new ResourceNotFoundException(httpStatus.getReasonPhrase());
            case CONFLICT:
                throw new DuplicateResourceException(httpStatus.getReasonPhrase());
            case INTERNAL_SERVER_ERROR:
                throw new ClientCallException(httpStatus.getReasonPhrase());
            default:
                throw new ClientCallException("received " + httpStatus.value() + " response with reason " + httpStatus.getReasonPhrase());
        }
    }
}
