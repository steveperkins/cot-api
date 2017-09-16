package org.collegeopentextbooks.api.model;

/**
 * A single review of a single textbook
 * @author Steve
 *
 */
public class BookReview {
	private Long id;
	private Book book;
	private Reviewer reviewer;
	private ReviewType reviewType;
	private String reviewImageUrl;
	private String comments;
	
	/**
	 * @return the review's ID
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the book on which this review was performed
	 */
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
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
	/**
	 * To be deprecated; use database-driven review classes instead
	 * @return the relative URL to the image of this review's rating graph
	 */
	public String getReviewImageUrl() {
		return reviewImageUrl;
	}
	public void setReviewImageUrl(String reviewImageUrl) {
		this.reviewImageUrl = reviewImageUrl;
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