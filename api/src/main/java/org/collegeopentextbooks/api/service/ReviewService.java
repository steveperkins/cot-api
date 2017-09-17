package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.db.ReviewDaoImpl;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewDaoImpl reviewDao;
	
	
	/**
	 * Retrieves ALL reviews regardless of resource or review type
	 * @return
	 */
	public List<Review> getReviews() {
		return reviewDao.getReviews();
	}
	
	/**
	 * Retrieves all reviews for the given resource ID and review type
	 * @param id the resource ID for which to search reviews
	 * @param reviewType the review type to filter on
	 * @return
	 */
	public List<Review> getReviews(Integer resourceId, ReviewType reviewType) {
		List<Review> reviews = reviewDao.getReviews(resourceId, reviewType);
		return reviews;
	}

	public Review getReview(Integer reviewId) {
		Review review = reviewDao.getById(reviewId);
		return review;
	}
	
	public Review save(Review review) {
		return reviewDao.save(review);
	}
	
}
