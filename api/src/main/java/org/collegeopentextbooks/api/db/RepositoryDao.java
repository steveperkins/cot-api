package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Repository;

public interface RepositoryDao {

	List<Repository> getRepositories();

	Repository getById(int repositoryId);
	
	Repository getByName(String repositoryName);

	/**
	 * Creates or updates an repository
	 * @param repository the repository to create or update
	 * @return
	 * @author steve.perkins
	 */
	Repository save(Repository repository);

}