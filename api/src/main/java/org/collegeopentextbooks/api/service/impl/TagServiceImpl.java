package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.TagDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

	private static final Integer NAME_MAX_LENGTH = 255;
	
	@Autowired
	private TagDao tagDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.TagService#getAll()
	 */
	@Override
	public List<Tag> getAll() {
		return tagDao.getTags();
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.TagService#getByType(org.collegeopentextbooks.api.model.TagType)
	 */
	@Override
	public List<Tag> getByType(TagType tagType) {
		return tagDao.getTagsByType(tagType);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.TagService#getByResource(java.lang.Integer)
	 */
	@Override
	public List<Tag> getByResource(Integer resourceId) {
		return tagDao.getTagsByResourceId(resourceId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.TagService#getByName(java.lang.String)
	 */
	@Override
	public Tag getByName(String name) {
		return tagDao.getTagByName(name);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.TagService#save(org.collegeopentextbooks.api.model.Tag)
	 */
	@Override
	public Tag save(Tag tag) {
		if(null == tag)
			return null;
		
		if(null == tag.getTagType())
			throw new RequiredValueEmptyException("Tag type is required");
		
		if(StringUtils.isBlank(tag.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(tag.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		Tag existingTag = tagDao.getTagByName(tag.getName());
		if(null != existingTag) {
			tag.setId(existingTag.getId());
		}
		return tagDao.save(tag);
	}
}
