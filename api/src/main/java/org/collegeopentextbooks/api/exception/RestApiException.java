package org.collegeopentextbooks.api.exception;

/**
 * Transfer object for REST exceptions
 * @author steve.perkins
 *
 */
public class RestApiException {
	private Integer code;
	private String message;
	
	public RestApiException(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
