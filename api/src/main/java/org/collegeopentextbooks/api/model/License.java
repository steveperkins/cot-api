package org.collegeopentextbooks.api.model;

public class License {
	private String name;
	private String url;
	private String searchName;
	
	public License() {}
	public License(String id) {
		this(id, null);
	}
	public License(String name, String url) {
		this.name = name;
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof License))
			return false;
		
		return (null == obj && null == name) || this.name.equals(((License)obj).name);
	}
	
}
