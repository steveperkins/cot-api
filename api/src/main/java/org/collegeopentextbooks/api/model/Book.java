package org.collegeopentextbooks.api.model;

import java.util.List;

/**
 * A single textbook
 * @author Steve
 *
 */
public class Book {
	private Long id;
	private List<Author> authors;
	private String contentUrl;
	private String title;
	private String license;
	private String licenseUrl;
	private String reviewUrl;
	private String ancillariesUrl;
	private String accessibilityUrl;
	private List<Tag> tags;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the authors of this book
	 */
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	/**
	 * @return the URL to this book's actual content
	 */
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	/**
	 * @return this book's full title
	 */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the standardized license abbreviation for this book's license
	 */
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	/**
	 * @return the URL to this book's license
	 */
	public String getLicenseUrl() {
		return licenseUrl;
	}
	public void setLicenseUrl(String licenseUrl) {
		this.licenseUrl = licenseUrl;
	}
	/**
	 * @return the URL to this book's review
	 */
	public String getReviewUrl() {
		return reviewUrl;
	}
	public void setReviewUrl(String reviewUrl) {
		this.reviewUrl = reviewUrl;
	}
	/**
	 * @return the URL to this book's ancillaries
	 */
	public String getAncillariesUrl() {
		return ancillariesUrl;
	}
	public void setAncillariesUrl(String ancillariesUrl) {
		this.ancillariesUrl = ancillariesUrl;
	}
	/**
	 * @return the URL to this book's accessibility review
	 */
	public String getAccessibilityReviewUrl() {
		return accessibilityUrl;
	}
	public void setAccessibilityReviewUrl(String accessibilityUrl) {
		this.accessibilityUrl = accessibilityUrl;
	}
	/**
	 * @return the tags that categorize this book
	 */
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
}
