package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.EditorDao;
import org.collegeopentextbooks.api.model.Editor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class EditorDaoImpl implements EditorDao {
	
	private static String GET_EDITORS_SQL = "SELECT e.* FROM editor e";
	private static String GET_EDITOR_BY_ID_SQL = "SELECT e.* FROM editor e WHERE e.id=?";
	private static String GET_EDITORS_BY_RESOURCE_SQL = "SELECT e.* FROM resource_editor re INNER JOIN editor e ON re.editor_id=e.id WHERE re.resource_id=?";
	private static String UPDATE_SQL = "UPDATE editor SET name=:name, search_name=LOWER(:name) WHERE id=:id";
	
	private static String DELETE_EDITOR_FROM_RESOURCE_SQL = "DELETE FROM resource_editor WHERE resource_id=? AND editor_id=?";
	private static String ADD_EDITOR_TO_RESOURCE_SQL = DELETE_EDITOR_FROM_RESOURCE_SQL + "; INSERT INTO resource_editor(resource_id, editor_id) VALUES(?, ?)";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	private BeanPropertyRowMapper<Editor> rowMapper = BeanPropertyRowMapper.newInstance(Editor.class);
	
	@Autowired
	public EditorDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("editor")
			                .usingGeneratedKeyColumns("id");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.EditorDao#getEditors()
	 */
	@Override
	public List<Editor> getEditors() {
		List<Editor> results = jdbcTemplate.query(GET_EDITORS_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Editor>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.EditorDao#getById(int)
	 */
	@Override
	public Editor getById(int editorId) {
		Editor editor = jdbcTemplate.queryForObject(GET_EDITOR_BY_ID_SQL, new Integer[] { editorId }, rowMapper);
		return editor;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.EditorDao#getEditorsByResourceId(int)
	 */
	@Override
	public List<Editor> getEditorsByResourceId(int resourceId) {
		List<Editor> results = jdbcTemplate.query(GET_EDITORS_BY_RESOURCE_SQL, new Integer[] { resourceId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Editor>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.EditorDao#addEditorToResource(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void addEditorToResource(Integer resourceId, Integer editorId) {
		this.jdbcTemplate.update(ADD_EDITOR_TO_RESOURCE_SQL, resourceId, editorId, resourceId, editorId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.EditorDao#deleteEditorFromResource(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void deleteEditorFromResource(Integer resourceId, Integer editorId) {
		this.jdbcTemplate.update(DELETE_EDITOR_FROM_RESOURCE_SQL, resourceId, editorId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.EditorDao#save(org.collegeopentextbooks.api.model.Editor)
	 */
	@Override
	public Editor save(Editor editor) {
		if(null == editor.getId())
			return insert(editor);
		else
			return update(editor);
	}
	
	protected Editor insert(Editor editor) {
		Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("name", editor.getName());
        parameters.put("search_name", editor.getSearchName());
        Number newId = this.insert.executeAndReturnKey(parameters);
        editor.setId(newId.intValue());
        return editor;
	}
	
	protected Editor update(Editor editor) {
		SqlParameterSource parameters = new BeanPropertySqlParameterSource(editor);
		this.jdbcTemplate.update(UPDATE_SQL, parameters);
		return editor;
	}
	
}
