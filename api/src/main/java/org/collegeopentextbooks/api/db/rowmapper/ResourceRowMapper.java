package org.collegeopentextbooks.api.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.model.Resource;
import org.springframework.stereotype.Component;

/**
 * Maps a Resource, its corresponding Repository, and the repository's Organization. Expects the ResultSet to have all Repository fields prefixed with repository_ and all Organization fields prefixed with organization_/
 * @author steve.perkins
 *
 */
@Component
public class ResourceRowMapper extends AbstractRowMapper<Resource> {

	@Override
	public Resource mapRow(ResultSet rs, int rowNum) throws SQLException {
		Resource resource = new Resource();
		resource.setAncillariesUrl(rs.getString("ancillaries_url"));
		resource.setExternalReviewUrl(rs.getString("external_review_url"));
		resource.setTitle(rs.getString("title"));
		resource.setSearchTitle(rs.getString("search_title"));
		resource.setUrl(rs.getString("url"));
		resource.setExternalId(rs.getString("external_id"));
		
		Repository repository = new Repository();
		repository.setId(rs.getInt("repository_id"));
		repository.setName(rs.getString("repository_name"));
		repository.setUrl(rs.getString("repository_url"));
		repository.setSearchName(rs.getString("repository_search_name"));
		repository.setCreatedDate(rs.getTimestamp("repository_created_date"));
		repository.setUpdatedDate(rs.getTimestamp("repository_updated_date"));
		
		Organization organization = new Organization();
		organization.setId(rs.getInt("organization_id"));
		organization.setName(rs.getString("organization_name"));
		organization.setUrl(rs.getString("organization_url"));
		organization.setLogoUrl(rs.getString("organization_logo_url"));
		organization.setSearchName(rs.getString("organization_search_name"));
		organization.setCreatedDate(rs.getTimestamp("organization_created_date"));
		organization.setUpdatedDate(rs.getTimestamp("organization_updated_date"));
		repository.setOrganization(organization);
		
		resource.setRepository(repository);
		return super.mapRow(rs, resource);
	}

}
