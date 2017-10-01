package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Reviewer;

public interface ReviewerService {

	/**
	 * Retrieves ALL reviewers
	 * @return
	 */
	List<Reviewer> getReviewers();

	Reviewer getReviewer(Integer reviewerId);

	/**
	 * Creates or updates the given reviewer's scalar values.
	 * @param repository the reviewer to create or update
	 * @return the updated reviewer. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided name, title, or biography is longer than their respective max lengths
	 * @author steve.perkins
	 */
	Reviewer save(Reviewer reviewer);

}