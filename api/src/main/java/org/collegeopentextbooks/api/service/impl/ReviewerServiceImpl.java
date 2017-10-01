package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.ReviewerDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Reviewer;
import org.collegeopentextbooks.api.service.ReviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewerServiceImpl implements ReviewerService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	private static final Integer TITLE_MAX_LENGTH = 255;
	private static final Integer BIOGRAPHY_MAX_LENGTH = 2000;
	
	@Autowired
	private ReviewerDao reviewerDao;
	
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ReviewerService#getReviewers()
	 */
	@Override
	public List<Reviewer> getReviewers() {
		return reviewerDao.getReviewers();
	}

	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ReviewerService#getReviewer(java.lang.Integer)
	 */
	@Override
	public Reviewer getReviewer(Integer reviewerId) {
		Reviewer reviewer = reviewerDao.getById(reviewerId);
		return reviewer;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ReviewerService#save(org.collegeopentextbooks.api.model.Reviewer)
	 */
	@Override
	public Reviewer save(Reviewer reviewer) {
		if(null == reviewer)
			return null;
		
		if(StringUtils.isBlank(reviewer.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(reviewer.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(reviewer.getTitle()) 
				&& reviewer.getTitle().length() > TITLE_MAX_LENGTH)
			throw new ValueTooLongException("Title exceeds max length (" + TITLE_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(reviewer.getBiography()) 
				&& reviewer.getBiography().length() > BIOGRAPHY_MAX_LENGTH)
			throw new ValueTooLongException("Biography exceeds max length (" + BIOGRAPHY_MAX_LENGTH + ")");
		
		return reviewerDao.save(reviewer);
	}
	
}
