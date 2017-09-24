package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.ReviewDaoImpl;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

	private static final Integer COMMENTS_MAX_LENGTH = 2000;
	private static final Integer CHART_URL_MAX_LENGTH = 255;
	
	@Autowired
	private ReviewDaoImpl reviewDao;
	
	
	/**
	 * Retrieves ALL reviews regardless of resource or review type
	 * @return
	 * @author steve.perkins
	 */
	public List<Review> getReviews() {
		return reviewDao.getReviews();
	}
	
	/**
	 * Retrieves all reviews for the given resource ID and review type
	 * @param id the resource ID for which to search reviews
	 * @param reviewType the review type to filter on
	 * @return
	 * @author steve.perkins
	 */
	public List<Review> getReviews(Integer resourceId, ReviewType reviewType) {
		List<Review> reviews = reviewDao.getReviews(resourceId, reviewType);
		return reviews;
	}

	/**
	 * Retrieves a single review by review ID
	 * @param reviewId
	 * @return
	 * @author steve.perkins
	 */
	public Review getReview(Integer reviewId) {
		Review review = reviewDao.getById(reviewId);
		return review;
	}
	
	/**
	 * Creates or updates the given review's scalar values.
	 * @param repository the review to create or update
	 * @return the updated review. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided comments are missing or blank
	 * @throws ValueTooLongException if the provided comments or chart URL is longer than their respective max lengths
	 * @author steve.perkins
	 */
	public Review save(Review review) {
		if(null == review)
			return null;
		
		if(StringUtils.isBlank(review.getComments()))
			throw new RequiredValueEmptyException("Comments cannot be blank");
		
		if(review.getComments().length() > COMMENTS_MAX_LENGTH)
			throw new ValueTooLongException("Comments exceed max length (" + COMMENTS_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(review.getChartUrl()) 
				&& review.getChartUrl().length() > CHART_URL_MAX_LENGTH)
			throw new ValueTooLongException("Chart URL exceeds max length (" + CHART_URL_MAX_LENGTH + ")");
		
		return reviewDao.save(review);
	}
	
}
