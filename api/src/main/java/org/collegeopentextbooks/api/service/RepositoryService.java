package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Repository;

public interface RepositoryService {

	/**
	 * Retrieves ALL repositories
	 * @return
	 * @author steve.perkins
	 */
	List<Repository> getRepositories();

	/**
	 * Retrieves a repository by its ID
	 * @param resourceId
	 * @return
	 * @author steve.perkins
	 */
	Repository getRepository(Integer resourceId);
	
	/**
	 * Retrieves a repository by its name
	 * @param repositoryName
	 * @return
	 * @author steve.perkins
	 */
	Repository getRepository(String repositoryName);

	/**
	 * Creates or updates the given repository's scalar values.
	 * @param repository the repository to create or update
	 * @return the updated repository. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided name or URL is longer than their respective max lengths
	 * @author steve.perkins
	 */
	Repository save(Repository repository) throws RequiredValueEmptyException, ValueTooLongException;

}