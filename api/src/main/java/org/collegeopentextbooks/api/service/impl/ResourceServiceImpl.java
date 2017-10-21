package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.AuthorDao;
import org.collegeopentextbooks.api.db.EditorDao;
import org.collegeopentextbooks.api.db.LicenseDao;
import org.collegeopentextbooks.api.db.ResourceDao;
import org.collegeopentextbooks.api.db.TagDao;
import org.collegeopentextbooks.api.exception.InvalidAuthorException;
import org.collegeopentextbooks.api.exception.InvalidEditorException;
import org.collegeopentextbooks.api.exception.InvalidResourceException;
import org.collegeopentextbooks.api.exception.InvalidTagException;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.SearchCriteria;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {
	
	private static final Integer TITLE_MAX_LENGTH = 255;
	private static final Integer URL_MAX_LENGTH = 255;
	private static final Integer ANCILLARIES_URL_MAX_LENGTH = 255;
	private static final Integer EXTERNAL_REVIEW_URL_MAX_LENGTH = 255;
	private static final Integer LICENSE_ID_MAX_LENGTH = 4;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private TagDao tagDao;
	
	@Autowired
	private AuthorDao authorDao;
	
	@Autowired
	private EditorDao editorDao;
	
	@Autowired
	private LicenseDao licenseDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#search(org.collegeopentextbooks.api.model.SearchCriteria)
	 */
	@Override
	public List<Resource> search(SearchCriteria searchCriteria) {
		return populate(resourceDao.search(searchCriteria));
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#getResources()
	 */
	@Override
	public List<Resource> getResources() {
		List<Resource> resources = resourceDao.getResources();
		for(Resource resource: resources) {
			populate(resource);
		}
		return resources;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#getResource(java.lang.Integer)
	 */
	@Override
	public Resource getResource(Integer resourceId) {
		Resource resource = resourceDao.getById(resourceId);
		if(null == resource)
			return null;
		
		populate(resource);
		// TODO Since this is a single resource, also get its review summary
		return resource;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#getResourcesByTag(java.lang.Integer)
	 */
	@Override
	public List<Resource> getResourcesByTag(Integer tagId) {
		return populate(resourceDao.getByTagId(tagId));
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#getResourcesByAuthor(java.lang.Integer)
	 */
	@Override
	public List<Resource> getResourcesByAuthor(Integer authorId) {
		return populate(resourceDao.getByAuthorId(authorId));
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#getResourcesByEditor(java.lang.Integer)
	 */
	@Override
	public List<Resource> getResourcesByEditor(Integer editorId) {
		return populate(resourceDao.getByAuthorId(editorId));
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#getResourcesByRepository(java.lang.Integer)
	 */
	@Override
	public List<Resource> getResourcesByRepository(Integer repositoryId) {
		return populate(resourceDao.getByAuthorId(repositoryId));
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#addAuthorToResource(org.collegeopentextbooks.api.model.Resource, org.collegeopentextbooks.api.model.Author)
	 */
	@Override
	public Resource addAuthorToResource(Resource resource, Author author) {
		if(null == resource || null == resource.getId() || resource.getId() < 0)
			throw new InvalidResourceException("Invalid resource ID");
		if(null == author || null == author.getId() || author.getId() < 0)
			throw new InvalidAuthorException("Invalid author ID");
		
		authorDao.addAuthorToResource(resource.getId(), author.getId());
		
		return resource;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#addEditorToResource(org.collegeopentextbooks.api.model.Resource, org.collegeopentextbooks.api.model.Editor)
	 */
	@Override
	public Resource addEditorToResource(Resource resource, Editor editor) throws InvalidResourceException, InvalidEditorException {
		if(null == resource || null == resource.getId() || resource.getId() < 0)
			throw new InvalidResourceException("Invalid resource ID");
		if(null == editor || null == editor.getId() || editor.getId() < 0)
			throw new InvalidEditorException("Invalid editor ID");
		
		editorDao.addEditorToResource(resource.getId(), editor.getId());
		
		return resource;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#addTagToResource(org.collegeopentextbooks.api.model.Resource, org.collegeopentextbooks.api.model.Tag)
	 */
	@Override
	public Resource addTagToResource(Resource resource, Tag tag) throws InvalidResourceException, InvalidTagException {
		if(null == resource || null == resource.getId() || resource.getId() < 0)
			throw new InvalidResourceException("Invalid resource ID");
		if(null == tag || null == tag.getId() || tag.getId() < 0)
			throw new InvalidTagException("Invalid tag ID");
		
		tagDao.addTagToResource(resource.getId(), tag.getId());
		
		return resource;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#addLicenseToResource(org.collegeopentextbooks.api.model.Resource, java.lang.String)
	 */
	@Override
	public void addLicenseToResource(Resource resource, String licenseId) {
		if(null == resource || null == resource.getId())
			throw new RequiredValueEmptyException("Resource ID is required");
		
		if(StringUtils.isBlank(licenseId))
			throw new RequiredValueEmptyException("License Code cannot be blank");
			
		if(licenseId.length() > LICENSE_ID_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + LICENSE_ID_MAX_LENGTH + ")");
		
		licenseDao.addLicenseToResource(resource.getId(), licenseId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#deleteLicenseFromResource(java.lang.Integer, java.lang.String)
	 */
	@Override
	public void deleteLicenseFromResource(Integer resourceId, String licenseId) {
		if(null == resourceId)
			throw new RequiredValueEmptyException("Resource ID is required");
		
		if(StringUtils.isBlank(licenseId))
			throw new RequiredValueEmptyException("License Code cannot be blank");
		
		if(licenseId.length() > LICENSE_ID_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + LICENSE_ID_MAX_LENGTH + ")");
		
		licenseDao.deleteLicenseFromResource(resourceId, licenseId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.ResourceService#save(org.collegeopentextbooks.api.model.Resource)
	 */
	@Override
	public Resource save(Resource resource) {
		if(null == resource)
			return null;
		
		if(StringUtils.isBlank(resource.getTitle()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(resource.getTitle().length() > TITLE_MAX_LENGTH)
			throw new ValueTooLongException("Title exceeds max length (" + TITLE_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(resource.getUrl()) 
				&& resource.getUrl().length() > URL_MAX_LENGTH)
			throw new ValueTooLongException("URL exceeds max length (" + URL_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(resource.getAncillariesUrl()) 
				&& resource.getAncillariesUrl().length() > ANCILLARIES_URL_MAX_LENGTH)
			throw new ValueTooLongException("Ancillaries URL exceeds max length (" + ANCILLARIES_URL_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(resource.getExternalReviewUrl()) 
				&& resource.getExternalReviewUrl().length() > EXTERNAL_REVIEW_URL_MAX_LENGTH)
			throw new ValueTooLongException("External Review URL exceeds max length (" + EXTERNAL_REVIEW_URL_MAX_LENGTH + ")");
		
		Resource existingResource = resourceDao.getBySearchTerm(resource.getTitle());
		if(null != existingResource) {
			resource.setId(existingResource.getId());
		}
		return resourceDao.save(resource);
	}
	
	/**
	 * Populates non-scalar resource properties 
	 * @param resource
	 * @return
	 * @author steve.perkins
	 */
	protected Resource populate(Resource resource) {
		if(null == resource)
			return null;
		
		resource.setAuthors(authorDao.getAuthorsByResourceId(resource.getId()));
		resource.setEditors(editorDao.getEditorsByResourceId(resource.getId()));
		resource.setTags(tagDao.getTagsByResourceId(resource.getId()));
		resource.setLicenses(licenseDao.getLicensesByResourceId(resource.getId()));
		return resource;
	}
	
	protected List<Resource> populate(List<Resource> resources) {
		for(Resource resource: resources) {
			populate(resource);
		}
		return resources;
	}

	@Override
	public Resource importAndMerge(Resource resource) {
		if(null == resource)
			return null;
		
		Resource dbResource = null;
		// First try to find this resource by its external (repository-specific) ID
		if(StringUtils.isNotBlank(resource.getExternalId())) {
			dbResource = resourceDao.getByExternalId(resource.getExternalId());
		}
		// Then by its title
		if(null == dbResource) {
			dbResource = resourceDao.getBySearchTerm(resource.getSearchTitle());
		}
		// If we still haven't found it, create a new record
		if(null == dbResource) {
			dbResource = save(resource);
		} else {
			// Save the scalar properties of the resource
			dbResource.setAncillariesUrl(resource.getAncillariesUrl());
			dbResource.setExternalId(resource.getExternalId());
			dbResource.setExternalReviewUrl(resource.getExternalReviewUrl());
			dbResource.setTitle(resource.getTitle());
			dbResource.setUrl(resource.getUrl());
			dbResource = save(dbResource);
		}
		dbResource.setAuthors(authorDao.merge(dbResource, resource.getAuthors()));
		dbResource.setEditors(editorDao.merge(dbResource, resource.getEditors()));
		dbResource.setLicenses(licenseDao.merge(dbResource, resource.getLicenses()));
		// TODO Merge tags?
		
		return populate(dbResource);
	}
	
}
