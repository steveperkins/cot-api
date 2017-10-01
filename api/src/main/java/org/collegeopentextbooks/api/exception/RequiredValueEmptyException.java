package org.collegeopentextbooks.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a required value is empty or blank
 * @author steve.perkins
 *
 */
@ResponseException(code = HttpStatus.BAD_REQUEST, reason = "Value cannot be blank")
public class RequiredValueEmptyException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public RequiredValueEmptyException() {
		super();
	}

	public RequiredValueEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	public RequiredValueEmptyException(String message) {
		super(message);
	}

	public RequiredValueEmptyException(Throwable cause) {
		super(cause);
	}

}
