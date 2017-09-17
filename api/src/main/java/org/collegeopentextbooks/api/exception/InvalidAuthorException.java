package org.collegeopentextbooks.api.exception;

public class InvalidAuthorException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public InvalidAuthorException() {
		super();
	}

	public InvalidAuthorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAuthorException(String message) {
		super(message);
	}

	public InvalidAuthorException(Throwable cause) {
		super(cause);
	}

}
