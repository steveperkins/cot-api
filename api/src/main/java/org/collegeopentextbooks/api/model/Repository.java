package org.collegeopentextbooks.api.model;

import java.util.Date;

/**
 * A repository containing resources
 * @author steve.perkins
 *
 */
public class Repository extends AbstractModelObject {
	private Organization organization;
	private String name;
	private String url;
	private String searchName;
	private Date lastImportedDate;
	
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
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
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public Date getLastImportedDate() {
		return lastImportedDate;
	}
	public void setLastImportedDate(Date lastImportedDate) {
		this.lastImportedDate = lastImportedDate;
	}
}
