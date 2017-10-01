package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Editor;

public interface EditorDao {

	List<Editor> getEditors();

	Editor getById(int editorId);

	List<Editor> getEditorsByResourceId(int resourceId);

	/**
	 * Associates an existing resource with an existing editor
	 * @param resourceId
	 * @param editorId
	 * @author steve.perkins
	 */
	void addEditorToResource(Integer resourceId, Integer editorId);

	/**
	 * Removes an existing association between a resource and an editor
	 * @param resourceId
	 * @param editorId
	 * @author steve.perkins
	 */
	void deleteEditorFromResource(Integer resourceId, Integer editorId);

	/**
	 * Creates or updates an editor
	 * @param editor the editor to create or update
	 * @return
	 * @author steve.perkins
	 */
	Editor save(Editor editor);

}