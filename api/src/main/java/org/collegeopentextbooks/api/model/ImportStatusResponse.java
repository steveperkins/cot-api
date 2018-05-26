package org.collegeopentextbooks.api.model;

/**
 * Holds the current status of a harvest job
 * @author steve.perkins
 *
 */
public class ImportStatusResponse {
	private ImportStatus status;
	private String message;
	
	public ImportStatusResponse() {}
	public ImportStatusResponse(ImportStatus status) {
		this(status, null);
	}
	public ImportStatusResponse(ImportStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public ImportStatus getStatus() {
		return status;
	}
	public void setStatus(ImportStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
