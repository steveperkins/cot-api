package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.Resource;

public interface EditorDao {

	List<Editor> getEditors();

	Editor getById(int editorId);

	/**
	 * Retrieves an editor by name
	 * @param name
	 * @return the matching editor or null if no match is found
	 * @author steve.perkins
	 */
	Editor getBySearchTerm(String name);
	
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
	 * Merges <code>resource</code>'s editors into the data store. Creates and associates any editors that do not exist. Associates any editors that exist but are not associated with <code>resource</code>. Disassociates any current editors that are not in <code>resource</code>'s editor list.
	 * @param resource an existing resources with a set of editors in the "desired" state (i.e. the final list of the resource's editors regardless of whether they currently exist or are associated)
	 * @return
	 * @editor steve.perkins
	 */
	List<Editor> merge(Resource resource, List<Editor> editors);
	
	/**
	 * Creates or updates an editor
	 * @param editor the editor to create or update
	 * @return
	 * @author steve.perkins
	 */
	Editor save(Editor editor);

}