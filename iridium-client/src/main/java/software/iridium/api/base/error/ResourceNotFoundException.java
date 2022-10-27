package software.iridium.api.base.error;


public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7180682033933421062L;

    private static final String CODE = "404";

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public String getCode() {
        return CODE;
    }
}
