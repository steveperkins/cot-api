package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.EditorDao;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class EditorDaoImpl implements EditorDao {
	
	private static String GET_EDITORS_SQL = "SELECT e.* FROM editor e";
	private static String GET_EDITOR_BY_ID_SQL = "SELECT e.* FROM editor e WHERE e.id=?";
	private static String GET_EDITOR_BY_SEARCH_TERM_SQL = "SELECT e.* FROM editor e WHERE e.search_name=?";
	private static String GET_EDITORS_BY_RESOURCE_SQL = "SELECT e.* FROM resource_editor re INNER JOIN editor e ON re.editor_id=e.id WHERE re.resource_id=?";
	private static String UPDATE_SQL = "UPDATE editor SET name=?, search_name=LOWER(?) WHERE id=?";
	
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
		List<Editor> editors = jdbcTemplate.query(GET_EDITOR_BY_ID_SQL, new Integer[] { editorId }, rowMapper);
		if(null == editors || editors.size() < 1) {
			return null;
		}
		return editors.get(0);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.EditorDao#getBySearchTerm(java.lang.String)
	 */
	@Override
	public Editor getBySearchTerm(String name) {
		List<Editor> editors = jdbcTemplate.query(GET_EDITOR_BY_SEARCH_TERM_SQL, new String[] { name.toLowerCase() }, rowMapper);
		if(null != editors && !editors.isEmpty()) {
			return editors.get(0);
		}
		return null;
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
	 * @see org.collegeopentextbooks.api.db.impl.EditorDao#merge(org.collegeopentextbooks.api.model.Resource, java.util.List<Editor>)
	 */
	@Override
	public List<Editor> merge(Resource resource, List<Editor> editors) {
		List<Editor> finalEditors = new ArrayList<Editor>();
		if(!CollectionUtils.isEmpty(editors)) {
			for(Editor editor: editors) {
				Editor dbEditor = getBySearchTerm(editor.getSearchName());
				if(null == dbEditor) {
					dbEditor = save(editor);
				}
				// Editor is now guaranteed to have an ID
				finalEditors.add(dbEditor);
			}
			
			// Determine which editors have been added to this resource
			List<Editor> newEditors = new ArrayList<Editor>(editors);
			newEditors.removeAll(resource.getEditors());
			for(Editor editor: newEditors) {
				addEditorToResource(resource.getId(), editor.getId());
			}
			
			// Determine which editors have been removed
			resource.getEditors().removeAll(finalEditors);
			// Disassociate the removed editors from this resource
			for(Editor editor: resource.getEditors()) {
				deleteEditorFromResource(resource.getId(), editor.getId());
			}
		}
		resource.setEditors(finalEditors);
		return resource.getEditors();
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
		this.jdbcTemplate.update(UPDATE_SQL, editor.getName(), editor.getSearchName(), editor.getId());
		return editor;
	}
	
}
