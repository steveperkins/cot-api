package org.collegeopentextbooks.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/tag", "/tags" }, 
		consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiTagController {

	@Autowired
	private TagService tagService;
	
	@RequestMapping(method=RequestMethod.GET, value="/")
    @ResponseBody List<Tag> getAllTags() {
        return tagService.getAll();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/{tagType}")
    @ResponseBody List<Tag> getTagsByType(String tagType) {
		TagType type = TagType.fromString(tagType);
		if(null == type) {
			return new ArrayList<Tag>();
		}
		
        return tagService.getByType(type);
    }
}