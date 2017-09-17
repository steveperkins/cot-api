package org.collegeopentextbooks.api.service;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.db.AuthorDaoImpl;
import org.collegeopentextbooks.api.db.EditorDaoImpl;
import org.collegeopentextbooks.api.db.ResourceDaoImpl;
import org.collegeopentextbooks.api.db.TagDaoImpl;
import org.collegeopentextbooks.api.exception.InvalidAuthorException;
import org.collegeopentextbooks.api.exception.InvalidEditorException;
import org.collegeopentextbooks.api.exception.InvalidResourceException;
import org.collegeopentextbooks.api.exception.InvalidTagException;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
	
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
	 * @return
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
	 * @return
	 */
	public Resource addEditorToResource(Resource resource, Editor editor) {
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
	
	public Resource addTagToResource(Resource resource, Tag tag) {
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
	
	public Resource save(Resource resource) {
		return resourceDao.save(resource);
	}
	
	protected Resource populate(Resource resource) {
		if(null == resource)
			return null;
		
		resource.setAuthors(authorDao.getAuthorsByResourceId(resource.getId()));
		resource.setEditors(editorDao.getEditorsByResourceId(resource.getId()));
		resource.setTags(tagDao.getTagsByResourceId(resource.getId()));
		return resource;
	}
	
}
