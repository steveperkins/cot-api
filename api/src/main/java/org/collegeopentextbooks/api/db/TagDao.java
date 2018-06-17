package org.collegeopentextbooks.api.db;

import java.util.List;
import java.util.Map;

import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagSearchCriteria;
import org.collegeopentextbooks.api.model.TagType;

public interface TagDao {

	Tag getTag(Integer tagId);

	List<Tag> getTags();

	List<Tag> getTagsByParent(Integer parentTagId);

	List<Tag> getTagsByType(TagType tagType);

	List<Tag> getTagsByResourceId(Integer resourceId);

	Tag getTagByName(String name);
	
	Map<Tag, List<String>> getAllKeywords();

	/**
	 * Associates an existing resource with an existing tag
	 * @param resourceId
	 * @param tagId
	 * @author steve.perkins
	 */
	void addTagToResource(Integer resourceId, Integer tagId);

	/**
	 * Removes an existing association between a resource and a tag
	 * @param resourceId
	 * @param tagId
	 * @author steve.perkins
	 */
	void deleteTagFromResource(Integer resourceId, Integer tagId);

	/**
	 * Creates or updates a tag
	 * @param tag the tag to create or update
	 * @return
	 * @author steve.perkins
	 */
	Tag save(Tag tag);
	
	List<Tag> merge(Resource resource, List<Tag> tags);

	List<Tag> search(TagSearchCriteria searchCriterai);

}