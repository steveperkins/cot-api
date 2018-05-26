package org.collegeopentextbooks.api.model;

import java.util.List;

/**
 * Model for specifying search parameters
 * @author steve.perkins
 *
 */
public class SearchCriteria {
	private List<Integer> repositoryIds;
	private List<Integer> authorIds;
	private List<Integer> editorIds;
	private List<Integer> tagIds;
	
	private String partialTitle;
	private String partialUrl;
	private List<String> licenseCodes;
	private Boolean hasAncillaries;
	private Boolean hasReview;
	
	public List<Integer> getRepositoryIds() {
		return repositoryIds;
	}
	public void setRepositoryIds(List<Integer> repositoryIds) {
		this.repositoryIds = repositoryIds;
	}
	public List<Integer> getAuthorIds() {
		return authorIds;
	}
	public void setAuthorIds(List<Integer> authorIds) {
		this.authorIds = authorIds;
	}
	public List<Integer> getEditorIds() {
		return editorIds;
	}
	public void setEditorIds(List<Integer> editorIds) {
		this.editorIds = editorIds;
	}
	public List<Integer> getTagIds() {
		return tagIds;
	}
	public void setTagIds(List<Integer> tagIds) {
		this.tagIds = tagIds;
	}
	public String getPartialTitle() {
		return partialTitle;
	}
	public void setPartialTitle(String partialTitle) {
		this.partialTitle = partialTitle;
	}
	public String getPartialUrl() {
		return partialUrl;
	}
	public void setPartialUrl(String partialUrl) {
		this.partialUrl = partialUrl;
	}
	public List<String> getLicenseCodes() {
		return licenseCodes;
	}
	public void setLicenseCodes(List<String> licenseCodes) {
		this.licenseCodes = licenseCodes;
	}
	public Boolean getHasAncillaries() {
		return hasAncillaries;
	}
	public void setHasAncillary(Boolean hasAncillary) {
		this.hasAncillaries = hasAncillary;
	}
	public Boolean getHasReview() {
		return hasReview;
	}
	public void setHasReview(Boolean hasReview) {
		this.hasReview = hasReview;
	}
	
}
