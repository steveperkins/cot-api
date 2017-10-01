package org.collegeopentextbooks.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when an input argument's length exceeds the max length defined for that argument.
 * @author steve.perkins
 *
 */
@ResponseException(code = HttpStatus.BAD_REQUEST, reason = "Value exceeds maximum length")
public class ValueTooLongException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public ValueTooLongException() {
		super();
	}

	public ValueTooLongException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValueTooLongException(String message) {
		super(message);
	}

	public ValueTooLongException(Throwable cause) {
		super(cause);
	}

}
