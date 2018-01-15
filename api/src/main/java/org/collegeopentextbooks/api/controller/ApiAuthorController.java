package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/author", "/authors" }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiAuthorController {

	@Autowired
	private AuthorService authorService;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<Author> getAuthors() {
        return authorService.getAuthors();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{authorId}")
    @ResponseBody Author getAuthor(@PathVariable Integer authorId) {
        return authorService.getAuthor(authorId);
    }
	
}