package org.collegeopentextbooks.api.model;

/**
 * A single review of a single resource
 * @author steve.perkins
 *
 */
public class Review extends AbstractModelObject {
	private Resource resource;
	private Reviewer reviewer;
	private ReviewType reviewType;
	private Double score;
	private String chartUrl;
	private String comments;
	
	/**
	 * @return the resource on which this review was performed
	 */
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	/**
	 * 
	 * @return the author of this review
	 */
	public Reviewer getReviewer() {
		return reviewer;
	}
	public void setReviewer(Reviewer reviewer) {
		this.reviewer = reviewer;
	}
	/**
	 * @return the purpose of this review
	 */
	public ReviewType getReviewType() {
		return reviewType;
	}
	public void setReviewType(ReviewType reviewType) {
		this.reviewType = reviewType;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	/**
	 * To be deprecated; use database-driven review classes instead
	 * @return the relative URL to the image of this review's rating graph
	 */
	public String getChartUrl() {
		return chartUrl;
	}
	public void setChartUrl(String reviewImageUrl) {
		this.chartUrl = reviewImageUrl;
	}
	/**
	 * @return the reviewer's freeform evaluation of this review's book
	 */
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
