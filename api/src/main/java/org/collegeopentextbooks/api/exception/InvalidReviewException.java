package org.collegeopentextbooks.api.exception;

public class InvalidReviewException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public InvalidReviewException() {
		super();
	}

	public InvalidReviewException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidReviewException(String message) {
		super(message);
	}

	public InvalidReviewException(Throwable cause) {
		super(cause);
	}

}
