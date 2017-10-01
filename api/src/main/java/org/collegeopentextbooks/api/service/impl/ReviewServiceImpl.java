package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.ReviewDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.collegeopentextbooks.api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

	private static final Integer COMMENTS_MAX_LENGTH = 2000;
	private static final Integer CHART_URL_MAX_LENGTH = 255;
	
	@Autowired
	private ReviewDao reviewDao;
	
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ReviewService#getReviews()
	 */
	@Override
	public List<Review> getReviews() {
		return reviewDao.getReviews();
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ReviewService#getReviews(java.lang.Integer, org.collegeopentextbooks.api.model.ReviewType)
	 */
	@Override
	public List<Review> getReviews(Integer resourceId, ReviewType reviewType) {
		List<Review> reviews = reviewDao.getReviews(resourceId, reviewType);
		return reviews;
	}

	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ReviewService#getReview(java.lang.Integer)
	 */
	@Override
	public Review getReview(Integer reviewId) {
		Review review = reviewDao.getById(reviewId);
		return review;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ReviewService#save(org.collegeopentextbooks.api.model.Review)
	 */
	@Override
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
