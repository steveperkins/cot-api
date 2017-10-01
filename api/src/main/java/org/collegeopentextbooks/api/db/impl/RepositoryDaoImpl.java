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
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class RepositoryDaoImpl implements RepositoryDao {
	
	private static String GET_REPOSITORIES_SQL = "SELECT r.*, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM repository r INNER JOIN organization o ON r.organization_id=o.id";
	private static String GET_REPOSITORY_BY_ID_SQL = "SELECT r.*, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM repository r INNER JOIN organization o ON r.organization_id=o.id WHERE r.id=?";
	private static String UPDATE_SQL = "UPDATE repository SET name=:name, url=:url, organization_id=:organizationId, search_name=LOWER(:name) WHERE id=:id";
	
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
		Repository result = jdbcTemplate.queryForObject(GET_REPOSITORY_BY_ID_SQL, new Integer[] { repositoryId }, rowMapper);
		return result;
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
        Number newId = this.insert.executeAndReturnKey(parameters);
        repository.setId(newId.intValue());
        return repository;
	}
	
	protected Repository update(Repository repository) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(repository);
		this.jdbcTemplate.update(UPDATE_SQL, parameters);
		return repository;
	}
	
}
