package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.db.EditorDaoImpl;
import org.collegeopentextbooks.api.model.Editor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorService {
	
	@Autowired
	private EditorDaoImpl editorDao;
	
	/**
	 * Retrieves ALL editors
	 * @return
	 */
	public List<Editor> getEditors() {
		List<Editor> editors = editorDao.getEditors();
		return editors;
	}
	
	/**
	 * Retrieves an editor by it's ID
	 * @param editorId
	 * @return
	 */
	public Editor getEditor(Integer editorId) {
		Editor editor = editorDao.getById(editorId);
		return editor;
	}
	
	public Editor save(Editor editor) {
		return editorDao.save(editor);
	}
	
}
