package org.collegeopentextbooks.api.model;

/**
 * A category in the rubric under which a resource is reviewed
 * @author steve.perkins
 *
 */
public class ReviewCategory extends AbstractModelObject {
	private ReviewType reviewType;
	private String name;
	private String description;
	private int sortOrder;
	private Double minScore;
	private Double maxScore;
	
	public ReviewType getReviewType() {
		return reviewType;
	}
	public void setReviewType(ReviewType reviewType) {
		this.reviewType = reviewType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Double getMinScore() {
		return minScore;
	}
	public void setMinScore(Double minScore) {
		this.minScore = minScore;
	}
	public Double getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}
}
