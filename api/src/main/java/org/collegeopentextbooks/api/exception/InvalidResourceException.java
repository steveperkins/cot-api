package org.collegeopentextbooks.api.exception;

public class InvalidResourceException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public InvalidResourceException() {
		super();
	}

	public InvalidResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidResourceException(String message) {
		super(message);
	}

	public InvalidResourceException(Throwable cause) {
		super(cause);
	}

}
