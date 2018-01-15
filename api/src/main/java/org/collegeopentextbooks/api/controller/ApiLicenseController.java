package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/license", "/licenses" }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiLicenseController {

	@Autowired
	private LicenseService licenseServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<String> getAllLicenses() {
        return licenseServiceImpl.getAll();
    }
	
}