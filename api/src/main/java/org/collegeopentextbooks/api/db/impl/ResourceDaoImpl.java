package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.LicenseDao;
import org.collegeopentextbooks.api.db.ResourceDao;
import org.collegeopentextbooks.api.db.rowmapper.ResourceRowMapper;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class ResourceDaoImpl implements ResourceDao {
	private static final Logger logger = LoggerFactory.getLogger(ResourceDaoImpl.class);
	private static String GET_RESOURCES_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON rep.organization_id=o.id";
	private static String GET_RESOURCE_BY_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON rep.organization_id=o.id WHERE r.id=?";
	private static String GET_RESOURCES_BY_REPOSITORY_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON rep.organization_id=o.id WHERE rep.id=?";
	private static String GET_RESOURCES_BY_TAG_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN resource_tag rt ON r.id=rt.resource_id INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON rep.organization_id=o.id WHERE rt.tag_id=?";
	private static String GET_RESOURCES_BY_AUTHOR_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN resource_author ra ON r.id=ra.resource_id INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON rep.organization_id=o.id WHERE ra.author_id=?";
	private static String GET_RESOURCES_BY_EDITOR_ID_SQL = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN resource_editor re ON r.id=re.resource_id INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON rep.organization_id=o.id WHERE re.editor_id=?";
	private static String UPDATE_SQL = "UPDATE resource SET title=:title, url=:url, license=:license, search_title=LOWER(:title), ancillaries_url=:ancillariesUrl, external_review_url=:externalReviewUrl WHERE id=:id";
	
	private static String SEARCH_SQL_SELECT = "SELECT r.*, rep.id AS repository_id, rep.name AS repository_name, rep.url AS repository_url, rep.search_name AS repository_search_name, rep.created_date AS repository_created_date, rep.updated_date AS repository_updated_date,  o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM resource r INNER JOIN repository rep ON r.repository_id=rep.id INNER JOIN organization o ON rep.organization_id=o.id";
	
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
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#getResources()
	 */
	@Override
	public List<Resource> getResources() {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#getById(int)
	 */
	@Override
	public Resource getById(int resourceId) {
		Resource result = jdbcTemplate.queryForObject(GET_RESOURCE_BY_ID_SQL, new Integer[] { resourceId }, rowMapper);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#getByRepositoryId(int)
	 */
	@Override
	public List<Resource> getByRepositoryId(int repositoryId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_REPOSITORY_ID_SQL, new Integer[] { repositoryId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#getByTagId(int)
	 */
	@Override
	public List<Resource> getByTagId(int tagId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_TAG_ID_SQL, new Integer[] { tagId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#getByAuthorId(int)
	 */
	@Override
	public List<Resource> getByAuthorId(int authorId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_AUTHOR_ID_SQL, new Integer[] { authorId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#getByEditorId(int)
	 */
	@Override
	public List<Resource> getByEditorId(int editorId) {
		List<Resource> results = jdbcTemplate.query(GET_RESOURCES_BY_EDITOR_ID_SQL, new Integer[] { editorId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#search(org.collegeopentextbooks.api.model.SearchCriteria)
	 */
	@Override
	public List<Resource> search(SearchCriteria searchCriteria) {
		List<String> conditions = new ArrayList<String>();
		List<Object> arguments = new ArrayList<Object>();
		
		// Constrain to selected repositories
		if(null != searchCriteria.getRepositoryIds() 
				&& !searchCriteria.getRepositoryIds().isEmpty()) {
			List<Integer> list = searchCriteria.getRepositoryIds();
			String condition = "rep.id IN(";
			for(int x = 0; x < list.size(); x++) {
				if(x > 0) {
					condition += ",";
				}
				condition += "?";
				arguments.add(list.get(x));
			}
			condition += ")";
			conditions.add(condition);
		}
		
		// Constrain to selected authors
		if(null != searchCriteria.getAuthorIds() 
				&& !searchCriteria.getAuthorIds().isEmpty()) {
			List<Integer> list = searchCriteria.getAuthorIds();
			String condition = "(SELECT COUNT(resource_author.author_id) FROM resource_author WHERE resource_id=r.id AND author_id IN(";
			
			for(int x = 0; x < list.size(); x++) {
				if(x > 0) {
					condition += ",";
				}
				condition += "?";
				arguments.add(list.get(x));
			}
			condition += ")) > 0";
			conditions.add(condition);
		}
		
		// Constrain to selected editors
		if(null != searchCriteria.getEditorIds() 
				&& !searchCriteria.getEditorIds().isEmpty()) {
			List<Integer> list = searchCriteria.getEditorIds();
			String condition = "(SELECT COUNT(resource_editor.editor_id) FROM resource_editor WHERE resource_id=r.id AND editor_id IN(";
			
			for(int x = 0; x < list.size(); x++) {
				if(x > 0) {
					condition += ",";
				}
				condition += "?";
				arguments.add(list.get(x));
			}
			condition += ")) > 0";
			conditions.add(condition);
		}
		
		if(null != searchCriteria.getTagIds() 
				&& !searchCriteria.getTagIds().isEmpty()) {
			List<Integer> list = searchCriteria.getTagIds();
			String condition = "(SELECT COUNT(resource_tag.tag_id) FROM resource_tag WHERE resource_id=r.id AND tag_id IN(";
			
			for(int x = 0; x < list.size(); x++) {
				if(x > 0) {
					condition += ",";
				}
				condition += "?";
				arguments.add(list.get(x));
			}
			condition += ")) > 0";
			conditions.add(condition);
		}
		
		// Constrain to selected licenses
		if(null != searchCriteria.getLicenseCodes() 
				&& !searchCriteria.getLicenseCodes().isEmpty()) {
			List<String> list = searchCriteria.getLicenseCodes();
			String condition = "(SELECT COUNT(license_id) FROM resource_license WHERE resource_id=r.id AND license_id IN(";
			for(int x = 0; x < list.size(); x++) {
				// Ignore input that could be malicious
				String licenseId = list.get(x);
				if(StringUtils.isBlank(licenseId) 
						|| licenseId.length() < LicenseDao.LICENSE_ID_MAX_SIZE) {
					continue;
				}
				
				if(x > 0) {
					condition += ",";
				}
				condition += "?";
				arguments.add(licenseId);
			}
			condition += ")) > 0";
			conditions.add(condition);
		}
		
		// TODO Test for SQL injection
		if(StringUtils.isNotBlank(searchCriteria.getPartialTitle())
				&& searchCriteria.getPartialTitle().length() > 3) {
			conditions.add("r.search_title LIKE ?");
			arguments.add("%" + searchCriteria.getPartialTitle().toLowerCase() + "%");
		}
		if(StringUtils.isNotBlank(searchCriteria.getPartialUrl())
				&& searchCriteria.getPartialUrl().length() > 3) {
			conditions.add("r.url LIKE ?");
			arguments.add("%" + searchCriteria.getPartialUrl() + "%");
		}
		
		StringBuilder criteria = new StringBuilder(SEARCH_SQL_SELECT + " WHERE ");
		int count = 0;
		for(String condition: conditions) {
			if(count > 0)
				criteria.append(" AND ");
			
			criteria.append(condition);
			count++;
		}
		
		String query = criteria.toString();
		logger.info("Search query is: " + query);
		List<Resource> results = jdbcTemplate.query(query, arguments.toArray(), rowMapper);
		if(null == results) {
			results = new ArrayList<Resource>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ResourceDao#save(org.collegeopentextbooks.api.model.Resource)
	 */
	@Override
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
