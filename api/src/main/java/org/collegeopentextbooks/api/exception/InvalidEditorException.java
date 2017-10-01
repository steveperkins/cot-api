package org.collegeopentextbooks.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when one or more of the provided editor's properties fail input validation. 
 * @author steve.perkins
 *
 */
@ResponseException(code = HttpStatus.BAD_REQUEST, reason = "Invalid editor")
public class InvalidEditorException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public InvalidEditorException() {
		super();
	}

	public InvalidEditorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEditorException(String message) {
		super(message);
	}

	public InvalidEditorException(Throwable cause) {
		super(cause);
	}

}
