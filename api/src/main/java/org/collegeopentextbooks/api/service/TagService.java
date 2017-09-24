package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.TagDaoImpl;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

	private static final Integer NAME_MAX_LENGTH = 255;
	
	@Autowired
	private TagDaoImpl tagDao;
	
	public List<Tag> getAll() {
		return tagDao.getTags();
	}
	
	public List<Tag> getByType(TagType tagType) {
		return tagDao.getTagsByType(tagType);
	}
	
	public List<Tag> getByResource(Integer resourceId) {
		return tagDao.getTagsByResourceId(resourceId);
	}
	
	public Tag getByName(String name) {
		return tagDao.getTagByName(name);
	}
	
	/**
	 * Creates or updates the given tag's scalar values.
	 * @param tag the tag to create or update
	 * @return the updated tag. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided tag name or type is missing or blank
	 * @throws ValueTooLongException if the provided tag name is longer than its max length
	 * @author steve.perkins
	 */
	public Tag save(Tag tag) {
		if(null == tag)
			return null;
		
		if(null == tag.getTagType())
			throw new RequiredValueEmptyException("Tag type is required");
		
		if(StringUtils.isBlank(tag.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(tag.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		return tagDao.save(tag);
	}
}
