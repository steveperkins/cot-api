package org.collegeopentextbooks.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when one or more of the provided review's properties fail input validation. 
 * @author steve.perkins
 *
 */
@ResponseException(code = HttpStatus.BAD_REQUEST, reason = "Invalid review")
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
