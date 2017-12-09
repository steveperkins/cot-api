package org.collegeopentextbooks.api.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class StringRowMapper implements RowMapper<String> {
	private String columnName;
	public StringRowMapper(String columnName) {
		this.columnName = columnName;
	}
	
	@Override
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		return rs.getString(columnName);
	}

}
