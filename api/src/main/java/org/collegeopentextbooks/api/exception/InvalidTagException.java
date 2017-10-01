package org.collegeopentextbooks.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when one or more of the provided tag's properties fail input validation. 
 * @author steve.perkins
 *
 */
@ResponseException(code = HttpStatus.BAD_REQUEST, reason = "Invalid tag")
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
