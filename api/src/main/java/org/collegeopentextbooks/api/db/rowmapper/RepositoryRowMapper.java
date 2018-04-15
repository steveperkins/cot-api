package org.collegeopentextbooks.api.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.model.Repository;
import org.springframework.stereotype.Component;

/**
 * Maps a Repository and its corresponding Organization. Expects the ResultSet to have all Organization fields prefixed with organization_/
 * @author steve.perkins
 *
 */
@Component
public class RepositoryRowMapper extends AbstractRowMapper<Repository> {

	@Override
	public Repository mapRow(ResultSet rs, int rowNum) throws SQLException {
		Repository repository = new Repository();
		repository.setName(rs.getString("name"));
		repository.setUrl(rs.getString("url"));
		repository.setSearchName(rs.getString("search_name"));
		repository.setLastImportedDate(rs.getTimestamp("last_imported_date"));
		
		Organization organization = new Organization();
		organization.setId(rs.getInt("organization_id"));
		organization.setName(rs.getString("organization_name"));
		organization.setUrl(rs.getString("organization_url"));
		organization.setLogoUrl(rs.getString("organization_logo_url"));
		organization.setSearchName(rs.getString("organization_search_name"));
		organization.setCreatedDate(rs.getTimestamp("organization_created_date"));
		organization.setUpdatedDate(rs.getTimestamp("organization_updated_date"));
		repository.setOrganization(organization);
		
		return super.mapRow(rs, repository);
	}

}
