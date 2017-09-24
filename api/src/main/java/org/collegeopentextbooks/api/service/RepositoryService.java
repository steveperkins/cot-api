package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.RepositoryDaoImpl;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	private static final Integer URL_MAX_LENGTH = 255;
	
	@Autowired
	private RepositoryDaoImpl repositoryDao;
	
	/**
	 * Retrieves ALL repositories
	 * @return
	 * @author steve.perkins
	 */
	public List<Repository> getRepositories() {
		List<Repository> repositories = repositoryDao.getRepositories();
		return repositories;
	}
	
	/**
	 * Retrieves a repository by it's ID
	 * @param resourceId
	 * @return
	 * @author steve.perkins
	 */
	public Repository getRepository(Integer resourceId) {
		Repository repository = repositoryDao.getById(resourceId);
		return repository;
	}
	
	/**
	 * Creates or updates the given repository's scalar values.
	 * @param repository the repository to create or update
	 * @return the updated repository. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided name or URL is longer than their respective max lengths
	 * @author steve.perkins
	 */
	public Repository save(Repository repository) throws RequiredValueEmptyException, ValueTooLongException {
		if(null == repository)
			return null;
		
		if(StringUtils.isBlank(repository.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(repository.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(repository.getUrl()) 
				&& repository.getUrl().length() > URL_MAX_LENGTH)
			throw new ValueTooLongException("URL exceeds max length (" + URL_MAX_LENGTH + ")");
		
		return repositoryDao.save(repository);
	}
	
}
