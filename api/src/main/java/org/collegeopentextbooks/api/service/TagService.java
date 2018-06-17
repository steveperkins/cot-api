package org.collegeopentextbooks.api.service;

import java.util.List;
import java.util.Map;

import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagSearchCriteria;
import org.collegeopentextbooks.api.model.TagType;

public interface TagService {

	List<Tag> getAll();

	List<Tag> getByType(TagType tagType);
	
	List<Tag> search(TagSearchCriteria critera);

	List<Tag> getByResource(Integer resourceId);

	Tag getByName(String name);
	
	/**
	 * Retrieves the tags associated with the given resource
	 * @param resource
	 * @return
	 * @author steve.perkins
	 */
	List<Tag> getTags(Resource resource);
	
	Map<Tag, List<String>> getAllKeywords();
	/**
	 * Creates or updates the given tag's scalar values.
	 * @param tag the tag to create or update
	 * @return the updated tag. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided tag name or type is missing or blank
	 * @throws ValueTooLongException if the provided tag name is longer than its max length
	 * @author steve.perkins
	 */
	Tag save(Tag tag);
	
	void addTagToResource(Resource resource, Tag tag);

}