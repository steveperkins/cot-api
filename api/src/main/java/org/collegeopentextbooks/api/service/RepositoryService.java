package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.db.RepositoryDaoImpl;
import org.collegeopentextbooks.api.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {
	
	@Autowired
	private RepositoryDaoImpl repositoryDao;
	
	/**
	 * Retrieves ALL repositories
	 * @return
	 */
	public List<Repository> getRepositories() {
		List<Repository> repositories = repositoryDao.getRepositories();
		return repositories;
	}
	
	/**
	 * Retrieves a repository by it's ID
	 * @param resourceId
	 * @return
	 */
	public Repository getRepository(Integer resourceId) {
		Repository repository = repositoryDao.getById(resourceId);
		return repository;
	}
	
	public Repository save(Repository repository) {
		return repositoryDao.save(repository);
	}
	
}
