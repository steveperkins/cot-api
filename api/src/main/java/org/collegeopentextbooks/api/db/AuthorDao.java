package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Author;

public interface AuthorDao {

	List<Author> getAuthors();

	Author getById(Integer authorId);

	List<Author> getAuthorsByResourceId(Integer resourceId);

	/**
	 * Associates an existing resource with an existing author
	 * @param resourceId
	 * @param authorId
	 * @author steve.perkins
	 */
	void addAuthorToResource(Integer resourceId, Integer authorId);

	/**
	 * Removes an existing association between a resource and an author
	 * @param resourceId
	 * @param authorId
	 * @author steve.perkins
	 */
	void deleteAuthorFromResource(Integer resourceId, Integer authorId);

	/**
	 * Creates or updates an author
	 * @param author the author to create or update
	 * @return
	 * @author steve.perkins
	 */
	Author save(Author author);

}