package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Resource;

public interface AuthorDao {

	List<Author> getAuthors();

	Author getById(Integer authorId);

	/**
	 * Retrieves an author by name
	 * @param name
	 * @return the matching author or null if no match is found
	 * @author steve.perkins
	 */
	Author getBySearchTerm(String name);
	
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
	 * Merges <code>resource</code>'s authors into the data store. Creates and associates any authors that do not exist. Associates any authors that exist but are not associated with <code>resource</code>. Disassociates any current authors that are not in <code>resource</code>'s author list.
	 * @param resource an existing resources with a set of authors in the "desired" state (i.e. the final list of the resource's authors regardless of whether they currently exist or are associated)
	 * @return
	 * @author steve.perkins
	 */
	List<Author> merge(Resource resource, List<Author> authors);
	
	/**
	 * Creates or updates an author
	 * @param author the author to create or update
	 * @return
	 * @author steve.perkins
	 */
	Author save(Author author);
	
}