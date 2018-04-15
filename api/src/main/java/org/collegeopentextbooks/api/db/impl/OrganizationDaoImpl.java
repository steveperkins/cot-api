package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.OrganizationDao;
import org.collegeopentextbooks.api.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class OrganizationDaoImpl implements OrganizationDao {
	
	private static String GET_ORGANIZATIONS_SQL = "SELECT o.* FROM organization o";
	private static String GET_ORGANIZATION_BY_ID_SQL = "SELECT o.* FROM organization o WHERE o.id=?";
	private static String GET_ORGANIZATION_BY_NAME_SQL = "SELECT o.* FROM organization o WHERE o.search_name=?";
	private static String UPDATE_SQL = "UPDATE organization SET name=:name, url=:url, logo_url=:logoUrl, search_name=LOWER(:name) WHERE id=:id";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	private BeanPropertyRowMapper<Organization> rowMapper = BeanPropertyRowMapper.newInstance(Organization.class);
	
	@Autowired
	public OrganizationDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("organization")
			                .usingGeneratedKeyColumns("id");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.OrganizationDao#getOrganizations()
	 */
	@Override
	public List<Organization> getOrganizations() {
		List<Organization> results = jdbcTemplate.query(GET_ORGANIZATIONS_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Organization>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.OrganizationDao#getById(int)
	 */
	@Override
	public Organization getById(int organizationId) {
		List<Organization> organizations = jdbcTemplate.query(GET_ORGANIZATION_BY_ID_SQL, new Integer[] { organizationId }, rowMapper);
		if(null == organizations || organizations.size() < 1) {
			return null;
		}
		return organizations.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.OrganizationDao#getByName(String)
	 */
	@Override
	public Organization getByName(String name) {
		List<Organization> organizations = jdbcTemplate.query(GET_ORGANIZATION_BY_NAME_SQL, new String[] { name.toLowerCase() }, rowMapper);
		if(null != organizations && !organizations.isEmpty()) {
			return organizations.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.OrganizationDao#save(org.collegeopentextbooks.api.model.Organization)
	 */
	@Override
	public Organization save(Organization organization) {
		if(null == organization.getId())
			return insert(organization);
		else
			return update(organization);
	}
	
	protected Organization insert(Organization organization) {
		Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("name", organization.getName());
        parameters.put("url", organization.getUrl());
        parameters.put("logo_url", organization.getLogoUrl());
        parameters.put("search_name", organization.getSearchName());
        Number newId = this.insert.executeAndReturnKey(parameters);
        organization.setId(newId.intValue());
        return organization;
	}
	
	protected Organization update(Organization organization) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(organization);
		this.jdbcTemplate.update(UPDATE_SQL, parameters);
		return organization;
	}
	
}
