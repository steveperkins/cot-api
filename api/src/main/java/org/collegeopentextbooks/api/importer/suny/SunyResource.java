package org.collegeopentextbooks.api.importer.suny;

import java.util.Date;
import java.util.List;

public class SunyResource {
	private String id;
	private Date date;
	private SunyRendered guid;
	private Date modified;
	private String status;
	private SunyRendered title;
	private SunyRendered content;
	private Integer author;
	private List<Integer> categories;
	private List<Integer> tags;
	private List<Integer> sunyaffiliation;
	private SunyPureTaxonomies pure_taxonomies;
	private SunyLicense acf;
	private SunyLinks _links;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public SunyRendered getGuid() {
		return guid;
	}
	public void setGuid(SunyRendered guid) {
		this.guid = guid;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public SunyRendered getTitle() {
		return title;
	}
	public void setTitle(SunyRendered title) {
		this.title = title;
	}
	public SunyRendered getContent() {
		return content;
	}
	public void setContent(SunyRendered content) {
		this.content = content;
	}
	public Integer getAuthor() {
		return author;
	}
	public void setAuthor(Integer author) {
		this.author = author;
	}
	public List<Integer> getCategories() {
		return categories;
	}
	public void setCategories(List<Integer> categories) {
		this.categories = categories;
	}
	public List<Integer> getTags() {
		return tags;
	}
	public void setTags(List<Integer> tags) {
		this.tags = tags;
	}
	public List<Integer> getSunyaffiliation() {
		return sunyaffiliation;
	}
	public void setSunyaffiliation(List<Integer> sunyaffiliation) {
		this.sunyaffiliation = sunyaffiliation;
	}
	public SunyPureTaxonomies getPure_taxonomies() {
		return pure_taxonomies;
	}
	public void setPure_taxonomies(SunyPureTaxonomies pure_taxonomies) {
		this.pure_taxonomies = pure_taxonomies;
	}
	public SunyLicense getAcf() {
		return acf;
	}
	public void setAcf(SunyLicense acf) {
		this.acf = acf;
	}
	public SunyLinks get_links() {
		return _links;
	}
	public void set_links(SunyLinks _links) {
		this._links = _links;
	}
}
