package org.collegeopentextbooks.api.service;

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
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.SearchCriteria;
import org.collegeopentextbooks.api.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
	
	private static final Integer TITLE_MAX_LENGTH = 255;
	private static final Integer URL_MAX_LENGTH = 255;
	private static final Integer ANCILLARIES_URL_MAX_LENGTH = 255;
	private static final Integer EXTERNAL_REVIEW_URL_MAX_LENGTH = 255;
	private static final Integer LICENSE_ID_MAX_LENGTH = 2;
	
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
	
	/**
	 * Finds resources using the given criteria
	 * @param searchCriteria
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> search(SearchCriteria searchCriteria) {
		return populate(resourceDao.search(searchCriteria));
	}
	
	/**
	 * Retrieves ALL resources
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResources() {
		List<Resource> resources = resourceDao.getResources();
		for(Resource resource: resources) {
			populate(resource);
		}
		return resources;
	}
	
	/**
	 * Retrieves a resource by it's ID
	 * @param resourceId
	 * @return
	 * @author steve.perkins
	 */
	public Resource getResource(Integer resourceId) {
		Resource resource = resourceDao.getById(resourceId);
		if(null == resource)
			return null;
		
		populate(resource);
		// TODO Since this is a single resource, also get its review summary
		return resource;
	}
	
	/**
	 * Retrieves all resources associated with the given tag
	 * @param tagId
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResourcesByTag(Integer tagId) {
		return populate(resourceDao.getByTagId(tagId));
	}
	
	/**
	 * Retrieves all resources associated with the given author
	 * @param authorId
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResourcesByAuthor(Integer authorId) {
		return populate(resourceDao.getByAuthorId(authorId));
	}
	
	/**
	 * Retrieves all resources associated with the given editor
	 * @param editorId
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResourcesByEditor(Integer editorId) {
		return populate(resourceDao.getByAuthorId(editorId));
	}
	
	/**
	 * Retrieves all resources in the given repository
	 * @param repositoryId
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResourcesByRepository(Integer repositoryId) {
		return populate(resourceDao.getByAuthorId(repositoryId));
	}
	
	/**
	 * Associates an existing author to an existing resource
	 * @param resource
	 * @param author
	 * @throws InvalidResourceException if the given resource has no ID
	 * @throws InvalidEditorException if the given author has no ID
	 * @return
	 * @author steve.perkins
	 */
	public Resource addAuthorToResource(Resource resource, Author author) {
		if(null == resource || null == resource.getId() || resource.getId() < 0)
			throw new InvalidResourceException("Invalid resource ID");
		if(null == author || null == author.getId() || author.getId() < 0)
			throw new InvalidAuthorException("Invalid author ID");
		
		authorDao.addAuthorToResource(resource.getId(), author.getId());
		
		if(null == resource.getAuthors())
			resource.setAuthors(new ArrayList<Author>());
		
		resource.getAuthors().add(author);
		return resource;
	}
	
	/**
	 * Associates an existing editor to an existing resource
	 * @param resource
	 * @param editor
	 * @throws InvalidResourceException if the given resource has no ID
	 * @throws InvalidEditorException if the given editor has no ID
	 * @return
	 * @author steve.perkins
	 */
	public Resource addEditorToResource(Resource resource, Editor editor) throws InvalidResourceException, InvalidEditorException {
		if(null == resource || null == resource.getId() || resource.getId() < 0)
			throw new InvalidResourceException("Invalid resource ID");
		if(null == editor || null == editor.getId() || editor.getId() < 0)
			throw new InvalidEditorException("Invalid editor ID");
		
		editorDao.addEditorToResource(resource.getId(), editor.getId());
		
		if(null == resource.getEditors())
			resource.setEditors(new ArrayList<Editor>());
		
		resource.getEditors().add(editor);
		return resource;
	}
	
	/**
	 * Associates a tag to an existing resource
	 * @param resource
	 * @param tag
	 * @throws InvalidResourceException if the given resource has no ID
	 * @throws InvalidTagException if the given tag has no ID
	 * @return
	 * @author steve.perkins
	 */
	public Resource addTagToResource(Resource resource, Tag tag) throws InvalidResourceException, InvalidTagException {
		if(null == resource || null == resource.getId() || resource.getId() < 0)
			throw new InvalidResourceException("Invalid resource ID");
		if(null == tag || null == tag.getId() || tag.getId() < 0)
			throw new InvalidTagException("Invalid tag ID");
		
		tagDao.addTagToResource(resource.getId(), tag.getId());
		
		if(null == resource.getTags())
			resource.setTags(new ArrayList<Tag>());
		
		resource.getTags().add(tag);
		return resource;
	}
	
	/**
	 * Adds an association between a resource and a license
	 * @param resource
	 * @param licenseId
	 * @throws RequiredValueEmptyException if the provided resource ID or license ID is missing or blank
	 * @throws ValueTooLongException if the provided license ID is longer than its max length
	 * @author steve.perkins
	 */
	public void addLicenseToResource(Resource resource, String licenseId) {
		if(null == resource || null == resource.getId())
			throw new RequiredValueEmptyException("Resource ID is required");
		
		if(StringUtils.isBlank(licenseId))
			throw new RequiredValueEmptyException("License Code cannot be blank");
			
		if(licenseId.length() > LICENSE_ID_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + LICENSE_ID_MAX_LENGTH + ")");
		
		licenseDao.addLicenseToResource(resource.getId(), licenseId);
	}
	
	/**
	 * Removes an existing association between a resource and a license
	 * @param resourceId
	 * @param licenseId
	 * @throws RequiredValueEmptyException if the provided resource ID or license ID is missing or blank
	 * @throws ValueTooLongException if the provided license ID is longer than its max length
	 * @author steve.perkins
	 */
	public void deleteLicenseFromResource(Integer resourceId, String licenseId) {
		if(null == resourceId)
			throw new RequiredValueEmptyException("Resource ID is required");
		
		if(StringUtils.isBlank(licenseId))
			throw new RequiredValueEmptyException("License Code cannot be blank");
		
		if(licenseId.length() > LICENSE_ID_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + LICENSE_ID_MAX_LENGTH + ")");
		
		licenseDao.deleteLicenseFromResource(resourceId, licenseId);
	}
	
	/**
	 * Creates or updates the given resource's scalar values.
	 * @param repository the resource to create or update
	 * @return the updated resource. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided title, content URL, ancillaries URL, external review URL, or license code is longer than their respective max lengths
	 * @author steve.perkins
	 */
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
	
}
