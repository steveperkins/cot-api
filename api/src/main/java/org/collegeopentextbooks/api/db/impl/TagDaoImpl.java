package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.TagDao;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class TagDaoImpl implements TagDao {
	
	private static String GET_TAG_SQL = "SELECT t.* FROM tag t WHERE id=?";
	private static String GET_TAGS_BY_PARENT_SQL = "SELECT t.* FROM tag t WHERE parent_tag_id=?";
	private static String GET_TAGS_SQL = "SELECT t.* FROM tag t";
	private static String GET_TAGS_BY_TYPE_SQL = "SELECT t.* FROM tag t WHERE t.tag_type=?";
	private static String GET_TAGS_BY_RESOURCE_ID_SQL = "SELECT t.* FROM tag t INNER JOIN resource_tag rt ON t.id=rt.tag_id WHERE rt.resource_id=?";
	private static String GET_TAGS_BY_NAME_SQL = "SELECT t.* FROM tag t WHERE t.search_name=?";
	private static String UPDATE_SQL = "UPDATE tag SET name=?, search_name=LOWER(?), parent_tag_id=? WHERE id=?";
	
	private static String ADD_TAG_TO_RESOURCE_SQL = "DELETE FROM resource_tag rt WHERE rt.resource_id=? AND rt.tag_id=?; INSERT INTO resource_tag(resource_id, tag_id) VALUES(?, ?)";
	private static String DELETE_TAG_FROM_RESOURCE_SQL = "DELETE FROM resource_tag rt WHERE rt.resource_id=? AND rt.tag_id=?";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	private BeanPropertyRowMapper<Tag> rowMapper = BeanPropertyRowMapper.newInstance(Tag.class);
	
	@Autowired
	public TagDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("tag")
			                .usingGeneratedKeyColumns("id");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#getTag(java.lang.Integer)
	 */
	@Override
	public Tag getTag(Integer tagId) {
		Tag tag = jdbcTemplate.queryForObject(GET_TAG_SQL, new Integer[] { tagId }, rowMapper);
		List<Tag> children = getTagsByParent(tag.getId());
		if(!children.isEmpty())
			tag.setChildren(children);
		return tag;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#getTags()
	 */
	@Override
	public List<Tag> getTags() {
		List<Tag> results = jdbcTemplate.query(GET_TAGS_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Tag>();
		}
		for(Tag tag: results) {
			List<Tag> children = getTagsByParent(tag.getId());
			if(!children.isEmpty())
				tag.setChildren(children);
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#getTagsByParent(java.lang.Integer)
	 */
	@Override
	public List<Tag> getTagsByParent(Integer parentTagId) {
		List<Tag> results = jdbcTemplate.query(GET_TAGS_BY_PARENT_SQL, new Integer[] { parentTagId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Tag>();
		}
		for(Tag tag: results) {
			List<Tag> children = getTagsByParent(tag.getId());
			if(!children.isEmpty())
				tag.setChildren(children);
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#getTagsByType(org.collegeopentextbooks.api.model.TagType)
	 */
	@Override
	public List<Tag> getTagsByType(TagType tagType) {
		List<Tag> results = jdbcTemplate.query(GET_TAGS_BY_TYPE_SQL, new String[] { tagType.toString() }, rowMapper);
		if(null == results) {
			results = new ArrayList<Tag>();
		}
		for(Tag tag: results) {
			List<Tag> children = getTagsByParent(tag.getId());
			if(!children.isEmpty())
				tag.setChildren(children);
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#getTagsByResourceId(java.lang.Integer)
	 */
	@Override
	public List<Tag> getTagsByResourceId(Integer resourceId) {
		List<Tag> results = jdbcTemplate.query(GET_TAGS_BY_RESOURCE_ID_SQL, new Integer[] { resourceId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Tag>();
		}
		for(Tag tag: results) {
			List<Tag> children = getTagsByParent(tag.getId());
			if(!children.isEmpty())
				tag.setChildren(children);
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#getTagByName(java.lang.String)
	 */
	@Override
	public Tag getTagByName(String name) {
		List<Tag> results = jdbcTemplate.query(GET_TAGS_BY_NAME_SQL, new String[] { name.toLowerCase() }, rowMapper);
		if(null == results || results.size() < 1) {
			return null;
		}
		Tag tag = results.get(0);
		List<Tag> children = getTagsByParent(tag.getId());
		if(!children.isEmpty())
			tag.setChildren(children);
		return tag;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#addTagToResource(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void addTagToResource(Integer resourceId, Integer tagId) {
        jdbcTemplate.update(ADD_TAG_TO_RESOURCE_SQL, resourceId, tagId, resourceId, tagId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#deleteTagFromResource(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void deleteTagFromResource(Integer resourceId, Integer tagId) {
        jdbcTemplate.update(DELETE_TAG_FROM_RESOURCE_SQL, resourceId, tagId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.TagDao#save(org.collegeopentextbooks.api.model.Tag)
	 */
	@Override
	public Tag save(Tag tag) {
		if(null == tag.getId())
			return insert(tag);
		else
			return update(tag);
	}
	
	protected Tag insert(Tag tag) {
		if(null == tag.getTagType())
			tag.setTagType(TagType.GENERAL);
		
		Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("name", tag.getName());
        parameters.put("tag_type", tag.getTagType());
        parameters.put("parent_tag_id", tag.getParentTagId());
        parameters.put("search_name", tag.getSearchName());
        Number newId = this.insert.executeAndReturnKey(parameters);
        tag.setId(newId.intValue());
        return tag;
	}
	
	protected Tag update(Tag tag) {
		if(null == tag.getTagType())
			tag.setTagType(TagType.GENERAL);
		
		this.jdbcTemplate.update(UPDATE_SQL, tag.getName(), tag.getName(), tag.getParentTagId(), tag.getId());
		return tag;
	}
	
}
