package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.SearchCriteria;

public interface ResourceDao {

	List<Resource> getResources();

	Resource getById(int resourceId);
	
	Resource getBySearchTerm(int repositoryId, String searchTerm);

	List<Resource> getByRepositoryId(int repositoryId);

	List<Resource> getByTagId(int tagId);

	List<Resource> getByAuthorId(int authorId);

	List<Resource> getByEditorId(int editorId);

	List<Resource> search(SearchCriteria searchCriteria);
	
	/**
	 * Creates or updates an resource
	 * @param resource the resource to create or update
	 * @return
	 * @author steve.perkins
	 */
	Resource save(Resource resource);

	/**
	 * Retrieves a resource by its external, repository-specific external ID
	 * @param externalId
	 * @return
	 * @author steve.perkins
	 */
	Resource getByExternalId(String externalId);

}