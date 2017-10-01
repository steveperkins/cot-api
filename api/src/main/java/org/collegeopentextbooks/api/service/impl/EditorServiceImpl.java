package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.EditorDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorServiceImpl implements EditorService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	
	@Autowired
	private EditorDao editorDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.EditorService#getEditors()
	 */
	@Override
	public List<Editor> getEditors() {
		List<Editor> editors = editorDao.getEditors();
		return editors;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.EditorService#getEditor(java.lang.Integer)
	 */
	@Override
	public Editor getEditor(Integer editorId) {
		Editor editor = editorDao.getById(editorId);
		return editor;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.EditorService#save(org.collegeopentextbooks.api.model.Editor)
	 */
	@Override
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
