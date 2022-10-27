package software.iridium.api.base.error;


public class DuplicateResourceException extends RuntimeException {

	private static final long serialVersionUID = 2114443363758574910L;

	private static final String CODE = "409";
	private static final String MESSAGE = "CONFLICT";

	public DuplicateResourceException() {
		super(MESSAGE);
	}
	
	public DuplicateResourceException(final String message) {
		super(message);
	}
	
	public String getCode() {
		return CODE;
	}
}
