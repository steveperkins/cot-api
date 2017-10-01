package org.collegeopentextbooks.api.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.collegeopentextbooks.api.model.AbstractModelObject;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractRowMapper<T extends AbstractModelObject> implements RowMapper<T> {

	protected T mapRow(ResultSet rs, T object) throws SQLException {
		// Map common fields
		object.setId(rs.getInt("id"));
		object.setCreatedDate(rs.getTimestamp("created_date"));
		object.setUpdatedDate(rs.getTimestamp("updated_date"));
		return object;
	}
}
