package org.collegeopentextbooks.api.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.rowmapper.ResourceRowMapper;
import org.collegeopentextbooks.api.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class ResourceDaoImpl {
	
	private static String GET_RESOURCES_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON r.organization_id=o.id";
	private static String GET_RESOURCE_BY_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON r.organization_id=o.id WHERE r.id=?";
	private static String GET_RESOURCES_BY_REPOSITORY_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON r.organization_id=o.id WHERE rep.id=?";
	private static String GET_RESOURCES_BY_TAG_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN resource_tag rt ON r.id=rt.resource_id INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON r.organization_id=o.id WHERE rt.tag_id=?";
	private static String GET_RESOURCES_BY_AUTHOR_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN resource_author ra ON r.id=ra.resource_id INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON r.organization_id=o.id WHERE ra.author_id=?";
	private static String GET_RESOURCES_BY_EDITOR_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN resource_editor re ON r.id=re.resource_id INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON r.organization_id=o.id WHERE re.editor_id=?";
	private static String UPDATE_SQL = "UPDATE resource SET title=:title, url=:url, license=:license, search_title=LOWER(:title), ancillaries_url=:ancillariesUrl, external_review_url=:externalReviewUrl WHERE id=:id";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	
	@Autowired
	private ResourceRowMapper rowMapper;
	
	@Autowired
	public ResourceDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("resource")
			                .usingGeneratedKeyColumns("id");
	}
	
	public List<Resource> getResources() {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	public Resource getById(int resourceId) {
		Resource result = jdbcTemplate.queryForObject(GET_RESOURCE_BY_ID_SQL, new Integer[] { resourceId }, rowMapper);
		return result;
	}
	
	public List<Resource> getByRepositoryId(int repositoryId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_REPOSITORY_ID_SQL, new Integer[] { repositoryId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	public List<Resource> getByTagId(int tagId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_TAG_ID_SQL, new Integer[] { tagId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	public List<Resource> getByAuthorId(int authorId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_AUTHOR_ID_SQL, new Integer[] { authorId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	public List<Resource> getByEditorId(int editorId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_EDITOR_ID_SQL, new Integer[] { editorId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	/**
	 * Creates or updates an resource
	 * @param resource the resource to create or update
	 * @return
	 * @author steve.perkins
	 */
	public Resource save(Resource resource) {
		if(null == resource.getId())
			return insert(resource);
		else
			return update(resource);
	}
	
	protected Resource insert(Resource resource) {
		Map<String, Object> parameters = new HashMap<String, Object>(8);
		parameters.put("repository_id", resource.getRepository().getId());
        parameters.put("title", resource.getTitle());
        parameters.put("url", resource.getUrl());
        parameters.put("license", resource.getLicense());
        parameters.put("search_title", resource.getSearchTitle());
        parameters.put("ancillaries_url", resource.getAncillariesUrl());
        parameters.put("external_review_url", resource.getExternalReviewUrl());
        parameters.put("search_title", resource.getSearchTitle());
        Number newId = this.insert.executeAndReturnKey(parameters);
        resource.setId(newId.intValue());
        return resource;
	}
	
	protected Resource update(Resource resource) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(resource);
		this.jdbcTemplate.update(UPDATE_SQL, parameters);
		return resource;
	}
	
}
