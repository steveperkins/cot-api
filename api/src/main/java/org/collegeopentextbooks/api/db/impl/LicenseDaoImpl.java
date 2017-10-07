package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.LicenseDao;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class LicenseDaoImpl implements LicenseDao {
	
	private static String GET_LICENSES_SQL = "SELECT l.* FROM license l";
	private static String GET_LICENSES_BY_RESOURCE_ID_SQL = "SELECT l.* FROM license l INNER JOIN resource_license rl ON l.id=rl.license_id WHERE rl.resource_id=?";
	private static String GET_LICENSE_BY_ID_SQL = "SELECT l.* FROM license l WHERE l.id=?";
	private static String UPDATE_SQL = "UPDATE license SET id=?, description=? WHERE id=?";
	
	private static String DELETE_LICENSE_FROM_RESOURCE_SQL = "DELETE FROM resource_license rl WHERE rl.resource_id=? AND rl.license_id=?";
	private static String ADD_LICENSE_TO_RESOURCE_SQL = DELETE_LICENSE_FROM_RESOURCE_SQL + "; INSERT INTO resource_license(resource_id, license_id) VALUES(?, ?)";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	private BeanPropertyRowMapper<License> rowMapper = BeanPropertyRowMapper.newInstance(License.class);
	
	@Autowired
	public LicenseDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("license");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#getLicenses()
	 */
	@Override
	public List<License> getLicenses() {
		List<License> results = jdbcTemplate.query(GET_LICENSES_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<License>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#getLicensesByResourceId(java.lang.Integer)
	 */
	@Override
	public List<License> getLicensesByResourceId(Integer resourceId) {
		List<License> results = jdbcTemplate.query(GET_LICENSES_BY_RESOURCE_ID_SQL, new Integer[] { resourceId }, rowMapper);
		if(null == results) {
			results = new ArrayList<License>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#getById(java.lang.String)
	 */
	@Override
	public License getById(String id) {
		List<License> results = jdbcTemplate.query(GET_LICENSE_BY_ID_SQL, new String[] { id.toUpperCase() }, rowMapper);
		if(null == results || results.size() < 1) {
			return null;
		}
		return results.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#addLicenseToResource(java.lang.Integer, java.lang.String)
	 */
	@Override
	public void addLicenseToResource(Integer resourceId, String licenseId) {
        jdbcTemplate.update(ADD_LICENSE_TO_RESOURCE_SQL, resourceId, licenseId, resourceId, licenseId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#deleteLicenseFromResource(java.lang.Integer, java.lang.String)
	 */
	@Override
	public void deleteLicenseFromResource(Integer resourceId, String licenseId) {
        jdbcTemplate.update(DELETE_LICENSE_FROM_RESOURCE_SQL, resourceId, licenseId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#merge(org.collegeopentextbooks.api.model.Resource, java.util.List)
	 */
	@Override
	public List<License> merge(Resource resource, List<License> licenses) {
		if(!CollectionUtils.isEmpty(resource.getLicenses())) {
			List<License> deletedLicenses = new ArrayList<License>(resource.getLicenses());
			deletedLicenses.removeAll(licenses);

			List<License> newLicenses = new ArrayList<License>(licenses);
			newLicenses.removeAll(resource.getLicenses());
			
			for(License license: deletedLicenses) {
				deleteLicenseFromResource(resource.getId(), license.getId());
			}
			
			for(License license: newLicenses) {
				addLicenseToResource(resource.getId(), license.getId());
			}
			
			resource.setLicenses(licenses);
			return resource.getLicenses();
		}
		return new ArrayList<License>();
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#insert(org.collegeopentextbooks.api.model.License)
	 */
	@Override
	public License insert(License license) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("id", license.getId());
        parameters.put("description", license.getDescription());
        this.insert.execute(parameters);
        return license;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.LicenseDao#update(org.collegeopentextbooks.api.model.License)
	 */
	@Override
	public License update(License license) {
		this.jdbcTemplate.update(UPDATE_SQL, new String[] { license.getId(), license.getDescription(), license.getId() });
		return license;
	}

}
