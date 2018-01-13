package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.LicenseDao;
import org.collegeopentextbooks.api.db.rowmapper.StringRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LicenseDaoImpl implements LicenseDao {
	private static String GET_LICENSES_SQL = "SELECT DISTINCT r.license_name FROM resource r";
	
	private JdbcTemplate jdbcTemplate;
	
	private StringRowMapper rowMapper = new StringRowMapper("license_name");
	
	@Autowired
	public LicenseDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.LicenseDao#getLicenses()
	 */
	@Override
	public List<String> getLicenses() {
		List<String> results = jdbcTemplate.query(GET_LICENSES_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<String>();
		}
		return results;
	}
	
}
