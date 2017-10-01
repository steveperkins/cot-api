package org.collegeopentextbooks.api.service;

import java.util.List;

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

public interface ResourceService {

	/**
	 * Finds resources using the given criteria
	 * @param searchCriteria
	 * @return
	 * @author steve.perkins
	 */
	List<Resource> search(SearchCriteria searchCriteria);

	/**
	 * Retrieves ALL resources
	 * @return
	 * @author steve.perkins
	 */
	List<Resource> getResources();

	/**
	 * Retrieves a resource by it's ID
	 * @param resourceId
	 * @return
	 * @author steve.perkins
	 */
	Resource getResource(Integer resourceId);

	/**
	 * Retrieves all resources associated with the given tag
	 * @param tagId
	 * @return
	 * @author steve.perkins
	 */
	List<Resource> getResourcesByTag(Integer tagId);

	/**
	 * Retrieves all resources associated with the given author
	 * @param authorId
	 * @return
	 * @author steve.perkins
	 */
	List<Resource> getResourcesByAuthor(Integer authorId);

	/**
	 * Retrieves all resources associated with the given editor
	 * @param editorId
	 * @return
	 * @author steve.perkins
	 */
	List<Resource> getResourcesByEditor(Integer editorId);

	/**
	 * Retrieves all resources in the given repository
	 * @param repositoryId
	 * @return
	 * @author steve.perkins
	 */
	List<Resource> getResourcesByRepository(Integer repositoryId);

	/**
	 * Associates an existing author to an existing resource
	 * @param resource
	 * @param author
	 * @throws InvalidResourceException if the given resource has no ID
	 * @throws InvalidEditorException if the given author has no ID
	 * @return
	 * @author steve.perkins
	 */
	Resource addAuthorToResource(Resource resource, Author author);

	/**
	 * Associates an existing editor to an existing resource
	 * @param resource
	 * @param editor
	 * @throws InvalidResourceException if the given resource has no ID
	 * @throws InvalidEditorException if the given editor has no ID
	 * @return
	 * @author steve.perkins
	 */
	Resource addEditorToResource(Resource resource, Editor editor)
			throws InvalidResourceException, InvalidEditorException;

	/**
	 * Associates a tag to an existing resource
	 * @param resource
	 * @param tag
	 * @throws InvalidResourceException if the given resource has no ID
	 * @throws InvalidTagException if the given tag has no ID
	 * @return
	 * @author steve.perkins
	 */
	Resource addTagToResource(Resource resource, Tag tag) throws InvalidResourceException, InvalidTagException;

	/**
	 * Adds an association between a resource and a license
	 * @param resource
	 * @param licenseId
	 * @throws RequiredValueEmptyException if the provided resource ID or license ID is missing or blank
	 * @throws ValueTooLongException if the provided license ID is longer than its max length
	 * @author steve.perkins
	 */
	void addLicenseToResource(Resource resource, String licenseId);

	/**
	 * Removes an existing association between a resource and a license
	 * @param resourceId
	 * @param licenseId
	 * @throws RequiredValueEmptyException if the provided resource ID or license ID is missing or blank
	 * @throws ValueTooLongException if the provided license ID is longer than its max length
	 * @author steve.perkins
	 */
	void deleteLicenseFromResource(Integer resourceId, String licenseId);

	/**
	 * Creates or updates the given resource's scalar values.
	 * @param repository the resource to create or update
	 * @return the updated resource. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided title, content URL, ancillaries URL, external review URL, or license code is longer than their respective max lengths
	 * @author steve.perkins
	 */
	Resource save(Resource resource);

}