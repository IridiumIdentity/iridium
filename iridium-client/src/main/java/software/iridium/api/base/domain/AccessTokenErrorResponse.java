package software.iridium.api.base.domain;

import java.io.Serializable;

public class AccessTokenErrorResponse implements Serializable {

    private static final long serialVersionUID = 6609889412637582655L;

    private String error;

    public AccessTokenErrorResponse() {
        super();
        this.error = "";
    }

    public AccessTokenErrorResponse(final String error) {
        this();
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String messages) {
        this.error = messages;
    }
}
