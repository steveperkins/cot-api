package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.ReviewerDao;
import org.collegeopentextbooks.api.model.Reviewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class ReviewerDaoImpl implements ReviewerDao {
	
	private static String GET_REVIEWERS_SQL = "SELECT r.*, rev.organization_id AS reviewer_organization_id, rev.name AS reviewer_name, rev.title AS reviewer_title, rev.biography AS reviewer_biography, rev.search_name AS reviewer_search_name, rev.created_date AS reviewer_created_date, rev.updated_date AS reviewer_updated_date, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM reviewer r INNER JOIN reviewer rev ON r.reviewer_id=rev.id INNER JOIN organization o ON rev.organization_id=o.id";
	private static String GET_REVIEWER_BY_ID_SQL = "SELECT r.*, rev.organization_id AS reviewer_organization_id, rev.name AS reviewer_name, rev.title AS reviewer_title, rev.biography AS reviewer_biography, rev.search_name AS reviewer_search_name, rev.created_date AS reviewer_created_date, rev.updated_date AS reviewer_updated_date, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS o.organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM reviewer r INNER JOIN reviewer rev ON r.reviewer_id=rev.id INNER JOIN organization o ON rev.organization_id=o.id WHERE r.id=?";
	private static String UPDATE_SQL = "UPDATE reviewer SET organization_id=:organizationId, name=:name, title=:title, biography=:biography, search_name=:LOWER(:name) WHERE id=:id";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	
	private BeanPropertyRowMapper<Reviewer> rowMapper = BeanPropertyRowMapper.newInstance(Reviewer.class);
	
	@Autowired
	public ReviewerDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("reviewer")
			                .usingGeneratedKeyColumns("id");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ReviewerDao#getReviewers()
	 */
	@Override
	public List<Reviewer> getReviewers() {
		List<Reviewer> results = jdbcTemplate.query(GET_REVIEWERS_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Reviewer>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ReviewerDao#getById(int)
	 */
	@Override
	public Reviewer getById(int reviewerId) {
		Reviewer result = jdbcTemplate.queryForObject(GET_REVIEWER_BY_ID_SQL, new Integer[] { reviewerId }, rowMapper);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ReviewerDao#save(org.collegeopentextbooks.api.model.Reviewer)
	 */
	@Override
	public Reviewer save(Reviewer reviewer) {
		if(null == reviewer.getId())
			return insert(reviewer);
		else
			return update(reviewer);
	}
	
	protected Reviewer insert(Reviewer reviewer) {
		Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("organization_id", reviewer.getOrganization().getId());
        parameters.put("name", reviewer.getName());
        parameters.put("title", reviewer.getTitle());
        parameters.put("biography", reviewer.getBiography());
        parameters.put("search_name", reviewer.getName().toLowerCase());
        Number newId = this.insert.executeAndReturnKey(parameters);
        reviewer.setId(newId.intValue());
        return reviewer;
	}
	
	protected Reviewer update(Reviewer reviewer) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(reviewer);
		this.jdbcTemplate.update(UPDATE_SQL, parameters);
		return reviewer;
	}
	
}
