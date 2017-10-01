package org.collegeopentextbooks.api.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.collegeopentextbooks.api.model.Reviewer;
import org.springframework.stereotype.Component;

/**
 * Maps a Repository and its corresponding Organization. Expects the ResultSet to have all Organization fields prefixed with organization_/
 * @author steve.perkins
 *
 */
@Component
public class ReviewRowMapper extends AbstractRowMapper<Review> {

	@Override
	public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
		Review review = new Review();
		review.setChartUrl(rs.getString("chart_url"));
		review.setComments(rs.getString("comments"));
		review.setReviewType(ReviewType.fromString(rs.getString("review_type")));
		review.setScore(rs.getDouble("score"));
		
		Resource resource = new Resource();
		resource.setId(rs.getInt("resource_id"));
		review.setResource(resource);
		
		Reviewer reviewer = new Reviewer();
		reviewer.setId(rs.getInt("reviewer_id"));
		reviewer.setBiography(rs.getString("reviewer_biography"));
		reviewer.setName(rs.getString("reviewer_name"));
		reviewer.setSearchName(rs.getString("reviewer_search_name"));
		reviewer.setTitle(rs.getString("reviewer_title"));
		reviewer.setCreatedDate(rs.getTimestamp("reviewer_created_date"));
		reviewer.setUpdatedDate(rs.getTimestamp("reviewer_updated_date"));
		review.setReviewer(reviewer);
		
		Organization organization = new Organization();
		organization.setId(rs.getInt("organization_id"));
		organization.setName(rs.getString("organization_name"));
		organization.setUrl(rs.getString("organization_url"));
		organization.setLogoUrl(rs.getString("organization_logo_url"));
		organization.setSearchName(rs.getString("organization_search_name"));
		organization.setCreatedDate(rs.getTimestamp("organization_created_date"));
		organization.setUpdatedDate(rs.getTimestamp("organization_updated_date"));
		reviewer.setOrganization(organization);
		
		return super.mapRow(rs, review);
	}

}
