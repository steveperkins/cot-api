package org.collegeopentextbooks.api.exception;

public class InvalidTagException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public InvalidTagException() {
		super();
	}

	public InvalidTagException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTagException(String message) {
		super(message);
	}

	public InvalidTagException(Throwable cause) {
		super(cause);
	}

}
