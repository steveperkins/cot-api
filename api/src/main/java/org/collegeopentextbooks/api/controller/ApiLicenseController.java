package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/license", "/licenses" }, 
		consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiLicenseController {

	@Autowired
	private LicenseService licenseServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<License> getAllLicenses() {
        return licenseServiceImpl.getAll();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{licenseCode}")
    @ResponseBody License getLicenseById(@PathVariable String licenseCode) {
		return licenseServiceImpl.getById(licenseCode);
    }
}