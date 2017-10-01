package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/repository", "/repositories" }, 
		consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiRepositoryController {

	@Autowired
	private RepositoryService repositoryServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<Repository> getRepositories() {
        return repositoryServiceImpl.getRepositories();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{repositoryId}")
    @ResponseBody Repository getRepository(@PathVariable Integer repositoryId) {
        return repositoryServiceImpl.getRepository(repositoryId);
    }
	
}