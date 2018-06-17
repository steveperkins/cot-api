package org.collegeopentextbooks.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagSearchCriteria;
import org.collegeopentextbooks.api.model.TagType;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/tag", "/tags" }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiTagController {

	@Autowired
	private TagService tagServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<Tag> getAllTags() {
        return tagServiceImpl.getAll();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{tagType}")
    @ResponseBody List<Tag> getTagsByType(@PathVariable String tagType) {
		TagType type = TagType.fromString(tagType);
		if(null == type) {
			return new ArrayList<Tag>();
		}
		
        return tagServiceImpl.getByType(type);
    }
	
	@RequestMapping(method=RequestMethod.POST, value="search")
	@ResponseBody List<Tag> getTagsByName(@RequestBody TagSearchCriteria searchCriteria) {
		return tagServiceImpl.search(searchCriteria);
	}
}