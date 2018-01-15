package org.collegeopentextbooks.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path="/version", 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiVersionController {

	@Value("${application.version}")
	private String applicationVersion;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody String getVersion() {
        return "{ \"version\": \"" + applicationVersion + "\" }";
    }
	
}