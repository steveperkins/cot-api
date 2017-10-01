package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Author;

public interface AuthorService {

	/**
	 * Retrieves ALL authors
	 * @return
	 * @author steve.perkins
	 */
	List<Author> getAuthors();

	/**
	 * Retrieves an author by it's ID
	 * @param authorId
	 * @return
	 * @author steve.perkins
	 */
	Author getAuthor(Integer authorId);

	/**
	 * Creates or updates the given author's scalar values.
	 * @param repository the author to create or update
	 * @return the updated author. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @author steve.perkins
	 */
	Author save(Author author) throws RequiredValueEmptyException, ValueTooLongException;

}