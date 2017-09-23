package org.collegeopentextbooks.api.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class LicenseDaoImpl {
	public static Integer LICENSE_ID_MAX_SIZE = 2;
	
	private static String GET_LICENSES_SQL = "SELECT l.* FROM license l";
	private static String GET_LICENSES_BY_RESOURCE_ID_SQL = "SELECT l.* FROM license l INNER JOIN resource_license rl ON l.id=rl.license_id WHERE rl.resource_id=?";
	private static String GET_LICENSE_BY_ID_SQL = "SELECT l.* FROM license l WHERE t.id=?";
	private static String UPDATE_SQL = "UPDATE license SET id=:id, description=:description WHERE id=:id";
	
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
	
	public List<License> getLicenses() {
		List<License> results = jdbcTemplate.query(GET_LICENSES_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<License>();
		}
		return results;
	}
	
	public List<License> getLicensesByResourceId(Integer resourceId) {
		List<License> results = jdbcTemplate.query(GET_LICENSES_BY_RESOURCE_ID_SQL, new Integer[] { resourceId }, rowMapper);
		if(null == results) {
			results = new ArrayList<License>();
		}
		return results;
	}
	
	public License getById(String id) {
		List<License> results = jdbcTemplate.query(GET_LICENSE_BY_ID_SQL, new String[] { id.toUpperCase() }, rowMapper);
		if(null == results || results.size() < 1) {
			return null;
		}
		return results.get(0);
	}
	
	/**
	 * Associates an existing resource with an existing license
	 * @param resourceId
	 * @param licenseId
	 * @author steve.perkins
	 */
	public void addLicenseToResource(Integer resourceId, String licenseId) {
        jdbcTemplate.update(ADD_LICENSE_TO_RESOURCE_SQL, resourceId, licenseId, resourceId, licenseId);
	}
	
	/**
	 * Removes an existing association between a resource and a license
	 * @param resourceId
	 * @param licenseId
	 * @author steve.perkins
	 */
	public void deleteLicenseFromResource(Integer resourceId, String licenseId) {
        jdbcTemplate.update(DELETE_LICENSE_FROM_RESOURCE_SQL, resourceId, licenseId);
	}
	
	/**
	 * Creates or updates a license
	 * @param license the license to create or update
	 * @return
	 * @author steve.perkins
	 */
	public License save(License license) {
		if(null == license.getId())
			return insert(license);
		else
			return update(license);
	}
	
	protected License insert(License license) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("id", license.getId());
        parameters.put("description", license.getDescription());
        this.insert.execute(parameters);
        return license;
	}
	
	protected License update(License license) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(license);
		this.jdbcTemplate.update(UPDATE_SQL, parameters);
		return license;
	}
	
}
