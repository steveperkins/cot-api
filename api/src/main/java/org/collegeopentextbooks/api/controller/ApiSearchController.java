package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.SearchCriteria;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path="/search")
public class ApiSearchController {

	@Autowired
	private TagService tagService;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<Resource> searchTest() {
        throw new ValueTooLongException("Wakka wakka waikiki!");
    }
	
	@RequestMapping(method=RequestMethod.POST, value="")
	@ResponseBody List<Resource> search(@RequestBody SearchCriteria searchCriteria) {
		throw new ValueTooLongException("Wakka wakka waikiki!");
	}
	
}