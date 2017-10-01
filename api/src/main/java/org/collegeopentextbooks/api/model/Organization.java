package org.collegeopentextbooks.api.model;

/**
 * An organization contributing reviewers, authors, or editors
 * @author steve.perkins
 *
 */
public class Organization extends AbstractModelObject {
	private String name;
	private String url;
	private String logoUrl;
	private String searchName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		if(null != name)
			setSearchName(name.toLowerCase());
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
}
