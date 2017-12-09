package org.collegeopentextbooks.api.service.impl;

import java.util.ArrayList;
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
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.SearchCriteria;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.service.AuthorService;
import org.collegeopentextbooks.api.service.EditorService;
import org.collegeopentextbooks.api.service.LicenseService;
import org.collegeopentextbooks.api.service.ResourceService;
import org.collegeopentextbooks.api.service.TagService;
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
	private TagService tagService;
	
	@Autowired
	private AuthorService authorService;
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private LicenseService licenseService;
	
	// In order to preserve the API's ease-of-use for consumers, we have to inject both the services and DAOs
	@Autowired
	private AuthorDao authorDao;
	
	@Autowired
	private EditorDao editorDao;
	
	@Autowired
	private LicenseDao licenseDao;
	
	@Autowired
	private TagDao tagDao;
	
	
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
		
		authorService.addAuthorToResource(resource, author);
		
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
		
		editorService.addEditorToResource(resource, editor);
		
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
		
		tagService.addTagToResource(resource, tag);
		
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
		
		licenseService.addLicenseToResource(resource, licenseId);
	}
	
	/**
	 * Removes an existing association between a resource and a license
	 * @param resourceId
	 * @param licenseId
	 * @throws RequiredValueEmptyException if the provided resource ID or license ID is missing or blank
	 * @throws ValueTooLongException if the provided license ID is longer than its max length
	 * @author steve.perkins
	 */
	public void deleteLicenseFromResource(Resource resource, String licenseId) {
		if(null == resource || null == resource.getId())
			throw new RequiredValueEmptyException("Resource ID is required");
		
		if(StringUtils.isBlank(licenseId))
			throw new RequiredValueEmptyException("License Code cannot be blank");
		
		if(licenseId.length() > LICENSE_ID_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + LICENSE_ID_MAX_LENGTH + ")");
		
		licenseService.deleteLicenseFromResource(resource, licenseId);
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
		resource = resourceDao.save(resource);
		
		// Now save all of the related pieces of this resource
		if(null != resource.getAuthors()) {
			List<Author> authors = new ArrayList<Author>(resource.getAuthors());
			for(Author author: authors) {
				authorService.save(author);
				addAuthorToResource(resource, author);
			}
		}
		if(null != resource.getEditors()) {
			for(Editor editor: resource.getEditors()) {
				editorService.save(editor);
				addEditorToResource(resource, editor);
			}
		}
		if(null != resource.getLicenses()) {
			for(License license: resource.getLicenses()) {
				addLicenseToResource(resource, license.getId());
			}
		}
		
		if(null != resource.getTags()) {
			List<Tag> tags = new ArrayList<Tag>(resource.getTags());
			for(Tag tag: tags) {
				tagService.save(tag);
				addTagToResource(resource, tag);
			}
		}
		
		return resource;
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
		
		resource.setAuthors(authorService.getAuthors(resource));
		resource.setEditors(editorService.getEditors(resource));
		resource.setTags(tagService.getTags(resource));
		resource.setLicenses(licenseService.getByResource(resource.getId()));
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
		dbResource.setTags(tagDao.merge(dbResource, resource.getTags()));
		
		return populate(dbResource);
	}
	
}
