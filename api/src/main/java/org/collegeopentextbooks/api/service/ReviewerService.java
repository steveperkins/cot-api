package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.db.ReviewerDaoImpl;
import org.collegeopentextbooks.api.model.Reviewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewerService {
	
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
	
	public Reviewer save(Reviewer reviewer) {
		return reviewerDao.save(reviewer);
	}
	
}
