package org.collegeopentextbooks.api.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.AuthorDaoImpl;
import org.collegeopentextbooks.api.db.EditorDaoImpl;
import org.collegeopentextbooks.api.db.ResourceDaoImpl;
import org.collegeopentextbooks.api.db.TagDaoImpl;
import org.collegeopentextbooks.api.exception.InvalidAuthorException;
import org.collegeopentextbooks.api.exception.InvalidEditorException;
import org.collegeopentextbooks.api.exception.InvalidResourceException;
import org.collegeopentextbooks.api.exception.InvalidTagException;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
	
	private static final Integer TITLE_MAX_LENGTH = 255;
	private static final Integer URL_MAX_LENGTH = 255;
	private static final Integer ANCILLARIES_URL_MAX_LENGTH = 255;
	private static final Integer EXTERNAL_REVIEW_URL_MAX_LENGTH = 255;
	private static final Integer LICENSE_MAX_LENGTH = 255;
	
	@Autowired
	private ResourceDaoImpl resourceDao;
	
	@Autowired
	private TagDaoImpl tagDao;
	
	@Autowired
	private AuthorDaoImpl authorDao;
	
	@Autowired
	private EditorDaoImpl editorDao;
	
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
		List<Resource> resources = resourceDao.getByTagId(tagId);
		for(Resource resource: resources) {
			populate(resource);
		}
		return resources;
	}
	
	/**
	 * Retrieves all resources associated with the given author
	 * @param authorId
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResourcesByAuthor(Integer authorId) {
		List<Resource> resources = resourceDao.getByAuthorId(authorId);
		for(Resource resource: resources) {
			populate(resource);
		}
		return resources;
	}
	
	/**
	 * Retrieves all resources associated with the given editor
	 * @param editorId
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResourcesByEditor(Integer editorId) {
		List<Resource> resources = resourceDao.getByAuthorId(editorId);
		for(Resource resource: resources) {
			populate(resource);
		}
		return resources;
	}
	
	/**
	 * Retrieves all resources in the given repository
	 * @param repositoryId
	 * @return
	 * @author steve.perkins
	 */
	public List<Resource> getResourcesByRepository(Integer repositoryId) {
		List<Resource> resources = resourceDao.getByAuthorId(repositoryId);
		for(Resource resource: resources) {
			populate(resource);
		}
		return resources;
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
		
		if(StringUtils.isNotBlank(resource.getLicense()) 
				&& resource.getLicense().length() > LICENSE_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + LICENSE_MAX_LENGTH + ")");
		
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
		return resource;
	}
	
}
