package software.iridium.api.base.error;

import java.io.Serializable;

public class AccessTokenErrorResponse implements Serializable {

    private static final long serialVersionUID = 1524799629250981083L;
    private String code;
    private String message;

    public AccessTokenErrorResponse() {
        super();
        this.code = "";
        this.message = "";
    }

    public AccessTokenErrorResponse(final String code, final String message) {
        this();
        this.setCode(code);
        this.setMessage(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String messages) {
        this.message = messages;
    }
}
