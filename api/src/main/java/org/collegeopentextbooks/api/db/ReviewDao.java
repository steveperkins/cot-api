package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;

public interface ReviewDao {

	List<Review> getReviews();

	Review getById(int reviewId);

	List<Review> getReviews(Integer resourceId, ReviewType reviewType);

	/**
	 * Creates or updates an review
	 * @param review the review to create or update
	 * @return
	 * @author steve.perkins
	 */
	Review save(Review review);

}