package org.collegeopentextbooks.api.model;

public class License {
	private String id;
	private String description;
	
	public License() {}
	public License(String id) {
		this(id, null);
	}
	public License(String id, String description) {
		this.id = id;
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof License))
			return false;
		
		return (null == obj && null == id) || this.id.equals(((License)obj).id);
	}
	
}
