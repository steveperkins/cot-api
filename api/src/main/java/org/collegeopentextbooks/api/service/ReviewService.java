package org.collegeopentextbooks.api.service;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.model.BookReview;
import org.collegeopentextbooks.api.model.ReviewType;
import org.collegeopentextbooks.api.model.Reviewer;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	
	/**
	 * Retrieves all reviews for the given book ID
	 * @param id the book ID for which to search reviews
	 * @return
	 */
	public List<BookReview> getReviews(Long bookId, ReviewType reviewType) {
		BookReview review = new BookReview();
		review.setId(1L);
		review.setComments("This book was terrible");
		review.setReviewImageUrl("http://www.dogpile.com");
		review.setReviewType(reviewType);
		
		Reviewer reviewer = new Reviewer();
		reviewer.setId(11L);
		reviewer.setName("George Galifinakas");
		review.setReviewer(reviewer);
		
		List<BookReview> reviews = new ArrayList<BookReview>();
		reviews.add(review);
		reviews.add(getReview(1L));
		return reviews;
	}

	public BookReview getReview(Long id) {
		BookReview review = new BookReview();
		review.setId(1L);
		review.setComments("I thought this was a great book");
		review.setReviewImageUrl("http://www.google.com");
		review.setReviewType(ReviewType.CONTENT);
		
		Reviewer reviewer = new Reviewer();
		reviewer.setId(10L);
		reviewer.setName("George Galifinakas");
		review.setReviewer(reviewer);
		return review;
	}
	
}