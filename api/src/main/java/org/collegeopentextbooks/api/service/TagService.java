package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.db.TagDaoImpl;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {
	
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
	
	public Tag save(Tag tag) {
		return tagDao.save(tag);
	}
}
