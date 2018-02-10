package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.ReviewDao;
import org.collegeopentextbooks.api.db.rowmapper.ReviewRowMapper;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class ReviewDaoImpl implements ReviewDao {
	
	private static String GET_REVIEWS_SQL = "SELECT r.*, rev.organization_id AS reviewer_organization_id, rev.name AS reviewer_name, rev.title AS reviewer_title, rev.biography AS reviewer_biography, rev.search_name AS reviewer_search_name, rev.created_date AS reviewer_created_date, rev.updated_date AS reviewer_updated_date, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM review r INNER JOIN reviewer rev ON r.reviewer_id=rev.id INNER JOIN organization o ON rev.organization_id=o.id";
	private static String GET_REVIEW_BY_ID_SQL = "SELECT r.*, rev.organization_id AS reviewer_organization_id, rev.name AS reviewer_name, rev.title AS reviewer_title, rev.biography AS reviewer_biography, rev.search_name AS reviewer_search_name, rev.created_date AS reviewer_created_date, rev.updated_date AS reviewer_updated_date, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM review r INNER JOIN reviewer rev ON r.reviewer_id=rev.id INNER JOIN organization o ON rev.organization_id=o.id WHERE r.id=?";
	private static String GET_REVIEWS_BY_RESOURCE_ID_AND_REVIEW_TYPE_SQL = "SELECT r.*, rev.organization_id AS reviewer_organization_id, rev.name AS reviewer_name, rev.title AS reviewer_title, rev.biography AS reviewer_biography, rev.search_name AS reviewer_search_name, rev.created_date AS reviewer_created_date, rev.updated_date AS reviewer_updated_date, o.id AS organization_id, o.name AS organization_name, o.url AS organization_url, o.logo_url AS organization_logo_url, o.search_name AS organization_search_name, o.created_date AS organization_created_date, o.updated_date AS organization_updated_date FROM review r INNER JOIN reviewer rev ON r.reviewer_id=rev.id INNER JOIN organization o ON rev.organization_id=o.id WHERE r.resource_id=? AND r.review_type=?";
	private static String UPDATE_SQL = "UPDATE review SET resource_id=:resourceId, reviewer_id=:reviewerId, review_type=:reviewType, score=:score, chart_url=:chartUrl, comments=:comments WHERE id=:id";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	
	@Autowired
	private ReviewRowMapper rowMapper;
	
	@Autowired
	public ReviewDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("review")
			                .usingGeneratedKeyColumns("id");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ReviewDao#getReviews()
	 */
	@Override
	public List<Review> getReviews() {
		List<Review> results = jdbcTemplate.query(GET_REVIEWS_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Review>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ReviewDao#getById(int)
	 */
	@Override
	public Review getById(int reviewId) {
		List<Review> results = jdbcTemplate.query(GET_REVIEW_BY_ID_SQL, new Integer[] { reviewId }, rowMapper);
		if(null == results) {
			return null;
		}
		return results.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ReviewDao#getReviews(java.lang.Integer, org.collegeopentextbooks.api.model.ReviewType)
	 */
	@Override
	public List<Review> getReviews(Integer resourceId, ReviewType reviewType) {
		List<Review> results = jdbcTemplate.query(GET_REVIEWS_BY_RESOURCE_ID_AND_REVIEW_TYPE_SQL, new Object[] { resourceId, reviewType.name() }, rowMapper);
		if(null == results) {
			results = new ArrayList<Review>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.ReviewDao#save(org.collegeopentextbooks.api.model.Review)
	 */
	@Override
	public Review save(Review review) {
		if(null == review.getId())
			return insert(review);
		else
			return update(review);
	}
	
	protected Review insert(Review review) {
		Map<String, Object> parameters = new HashMap<String, Object>(6);
        parameters.put("resource_id", review.getResource().getId());
        parameters.put("reviewer_id", review.getReviewer().getId());
        parameters.put("review_type", review.getReviewType());
        parameters.put("score", review.getScore());
        parameters.put("chart_url", review.getChartUrl());
        parameters.put("comments", review.getComments());
        Number newId = this.insert.executeAndReturnKey(parameters);
        review.setId(newId.intValue());
        return review;
	}
	
	protected Review update(Review review) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(review);
		this.jdbcTemplate.update(UPDATE_SQL, parameters);
		return review;
	}
	
}
