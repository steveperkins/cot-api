package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;

public interface ReviewService {

	/**
	 * Retrieves ALL reviews regardless of resource or review type
	 * @return
	 * @author steve.perkins
	 */
	List<Review> getReviews();

	/**
	 * Retrieves all reviews for the given resource ID and review type
	 * @param id the resource ID for which to search reviews
	 * @param reviewType the review type to filter on
	 * @return
	 * @author steve.perkins
	 */
	List<Review> getReviews(Integer resourceId, ReviewType reviewType);

	/**
	 * Retrieves a single review by review ID
	 * @param reviewId
	 * @return
	 * @author steve.perkins
	 */
	Review getReview(Integer reviewId);

	/**
	 * Creates or updates the given review's scalar values.
	 * @param repository the review to create or update
	 * @return the updated review. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided comments are missing or blank
	 * @throws ValueTooLongException if the provided comments or chart URL is longer than their respective max lengths
	 * @author steve.perkins
	 */
	Review save(Review review);

}