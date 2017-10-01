package org.collegeopentextbooks.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when one or more of the provided resource's properties fail input validation. 
 * @author steve.perkins
 *
 */
@ResponseException(code = HttpStatus.BAD_REQUEST, reason = "Invalid resource")
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
