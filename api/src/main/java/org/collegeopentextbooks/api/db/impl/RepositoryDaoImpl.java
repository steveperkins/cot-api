package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.RepositoryDao;
import org.collegeopentextbooks.api.db.rowmapper.RepositoryRowMapper;
import org.collegeopentextbooks.api.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class RepositoryDaoImpl implements RepositoryDao {
	
	private static String GET_REPOSITORIES_SQL = "SELECT r.*, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM repository r INNER JOIN organization o ON r.organization_id=o.id";
	private static String GET_REPOSITORY_BY_ID_SQL = "SELECT r.*, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM repository r INNER JOIN organization o ON r.organization_id=o.id WHERE r.id=?";
	private static String GET_REPOSITORY_BY_NAME_SQL = "SELECT r.*, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM repository r INNER JOIN organization o ON r.organization_id=o.id WHERE r.search_name=?";
	private static String UPDATE_SQL = "UPDATE repository SET name=?, url=?, organization_id=?, search_name=LOWER(?), last_imported_date=? WHERE id=?";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	
	@Autowired
	private RepositoryRowMapper rowMapper;
	
	@Autowired
	public RepositoryDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("repository")
			                .usingGeneratedKeyColumns("id");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.RepositoryDao#getRepositories()
	 */
	@Override
	public List<Repository> getRepositories() {
		List<Repository> results = jdbcTemplate.query(GET_REPOSITORIES_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Repository>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.RepositoryDao#getById(int)
	 */
	@Override
	public Repository getById(int repositoryId) {
		List<Repository> results = jdbcTemplate.query(GET_REPOSITORY_BY_ID_SQL, new Integer[] { repositoryId }, rowMapper);
		if(null == results || results.size() < 1) {
			return null;
		}
		return results.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.RepositoryDao#getByName(String)
	 */
	@Override
	public Repository getByName(String repositoryName) {
		List<Repository> results = jdbcTemplate.query(GET_REPOSITORY_BY_NAME_SQL, new String[] { repositoryName.toLowerCase() }, rowMapper);
		if(null != results&& !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.RepositoryDao#save(org.collegeopentextbooks.api.model.Repository)
	 */
	@Override
	public Repository save(Repository repository) {
		if(null == repository.getId())
			return insert(repository);
		else
			return update(repository);
	}
	
	protected Repository insert(Repository repository) {
		Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("name", repository.getName());
        parameters.put("url", repository.getUrl());
        parameters.put("organization_id", repository.getOrganization().getId());
        parameters.put("search_name", repository.getSearchName());
        parameters.put("last_imported_date", repository.getLastImportedDate());
        Number newId = this.insert.executeAndReturnKey(parameters);
        repository.setId(newId.intValue());
        return repository;
	}
	
	protected Repository update(Repository repository) {
		// This is more than a little ridiculous. BeanPropertySqlParameterSource and MapSqlParameterSource can't manage to properly convert a java.util.Date
		// to a valid SQL value, so we have to revert to old-school crud.
		java.sql.Timestamp sometimesSpringSucks = null;
		if(null != repository.getLastImportedDate()) {
			sometimesSpringSucks = new java.sql.Timestamp(repository.getLastImportedDate().getTime());
		}
		this.jdbcTemplate.update(UPDATE_SQL, new Object[] { repository.getName(), repository.getUrl(), repository.getOrganization().getId(), repository.getName(), sometimesSpringSucks, repository.getId() });
		return repository;
	}
	
}
