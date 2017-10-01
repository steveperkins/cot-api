package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.service.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/editor", "/editors" }, 
		consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiEditorController {

	@Autowired
	private EditorService editorServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<Editor> getEditors() {
        return editorServiceImpl.getEditors();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{editorId}")
    @ResponseBody Editor getEditor(@PathVariable Integer editorId) {
        return editorServiceImpl.getEditor(editorId);
    }
	
}