package org.collegeopentextbooks.api.model;

import java.util.List;

/**
 * A single textbook or other classroom resource
 * @author steve.perkins
 *
 */
public class Resource extends AbstractModelObject {
	private Repository repository;
	private List<Author> authors;
	private List<Editor> editors;
	private List<Tag> tags;
	private List<License> licenses;
	private List<Review> reviews;
	
	private String title;
	private String url;
	private String ancillariesUrl;
	private String externalReviewUrl;
	private String searchTitle;
	private String externalId;
	
	public Repository getRepository() {
		return repository;
	}
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public List<Editor> getEditors() {
		return editors;
	}
	public void setEditors(List<Editor> editors) {
		this.editors = editors;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public List<License> getLicenses() {
		return licenses;
	}
	public void setLicenses(List<License> licenses) {
		this.licenses = licenses;
	}
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		if(null != title)
			setSearchTitle(title.toLowerCase());
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAncillariesUrl() {
		return ancillariesUrl;
	}
	public void setAncillariesUrl(String ancillariesUrl) {
		this.ancillariesUrl = ancillariesUrl;
	}
	public String getExternalReviewUrl() {
		return externalReviewUrl;
	}
	public void setExternalReviewUrl(String externalReviewUrl) {
		this.externalReviewUrl = externalReviewUrl;
	}
	public String getSearchTitle() {
		return searchTitle;
	}
	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}
	/**
	 * Retrieves the repository-specific identifier for this resource
	 * @return
	 * @author steve.perkins
	 */
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

}
