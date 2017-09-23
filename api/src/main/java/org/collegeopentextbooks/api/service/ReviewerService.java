package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.ReviewerDaoImpl;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Reviewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewerService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	private static final Integer TITLE_MAX_LENGTH = 255;
	private static final Integer BIOGRAPHY_MAX_LENGTH = 2000;
	
	@Autowired
	private ReviewerDaoImpl reviewerDao;
	
	
	/**
	 * Retrieves ALL reviewers
	 * @return
	 */
	public List<Reviewer> getReviewers() {
		return reviewerDao.getReviewers();
	}

	public Reviewer getReviewer(Integer reviewerId) {
		Reviewer reviewer = reviewerDao.getById(reviewerId);
		return reviewer;
	}
	
	/**
	 * Creates or updates the given reviewer's scalar values.
	 * @param repository the reviewer to create or update
	 * @return the updated reviewer. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided name, title, or biography is longer than their respective max lengths
	 * @author steve.perkins
	 */
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
