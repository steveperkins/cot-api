package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Reviewer;

public interface ReviewerDao {

	List<Reviewer> getReviewers();

	Reviewer getById(int reviewerId);

	/**
	 * Creates or updates a reviewer
	 * @param reviewer the reviewer to create or update
	 * @return
	 * @author steve.perkins
	 */
	Reviewer save(Reviewer reviewer);

}