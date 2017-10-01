package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.EditorDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Editor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	
	@Autowired
	private EditorDao editorDao;
	
	/**
	 * Retrieves ALL editors
	 * @return
	 * @author steve.perkins
	 */
	public List<Editor> getEditors() {
		List<Editor> editors = editorDao.getEditors();
		return editors;
	}
	
	/**
	 * Retrieves an editor by it's ID
	 * @param editorId
	 * @return
	 * @author steve.perkins
	 */
	public Editor getEditor(Integer editorId) {
		Editor editor = editorDao.getById(editorId);
		return editor;
	}
	
	/**
	 * Creates or updates the given editor's scalar values.
	 * @param repository the editor to create or update
	 * @return the updated editor. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @author steve.perkins
	 */
	public Editor save(Editor editor) {
		if(null == editor)
			return editor;
		
		if(StringUtils.isBlank(editor.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(editor.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		return editorDao.save(editor);
	}
	
}
