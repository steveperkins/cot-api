package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.RepositoryDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryServiceImpl implements RepositoryService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	private static final Integer URL_MAX_LENGTH = 255;
	
	@Autowired
	private RepositoryDao repositoryDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.RepositoryService#getRepositories()
	 */
	@Override
	public List<Repository> getRepositories() {
		List<Repository> repositories = repositoryDao.getRepositories();
		return repositories;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.RepositoryService#getRepository(java.lang.Integer)
	 */
	@Override
	public Repository getRepository(Integer resourceId) {
		Repository repository = repositoryDao.getById(resourceId);
		return repository;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.RepositoryService#getRepository(java.lang.String)
	 */
	@Override
	public Repository getRepository(String repositoryName) {
		Repository repository = repositoryDao.getByName(repositoryName);
		return repository;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.RepositoryService#save(org.collegeopentextbooks.api.model.Repository)
	 */
	@Override
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
		
		Repository existingRepository = repositoryDao.getByName(repository.getName());
		if(null != existingRepository) {
			repository.setId(existingRepository.getId());
		}
		return repositoryDao.save(repository);
	}
	
}
