package org.collegeopentextbooks.api.exception;

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
