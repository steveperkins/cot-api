package org.collegeopentextbooks.api.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.springframework.stereotype.Component;

@Component
public class TagRowMapper extends AbstractRowMapper<Tag> {

	@Override
	public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
		Tag tag = new Tag();
		tag.setName(rs.getString("name"));
		tag.setTagType(TagType.fromString(rs.getString("tag_type")));
		return super.mapRow(rs, tag);
	}

}
