package org.collegeopentextbooks.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.importer.CotHtmlImporter;
import org.collegeopentextbooks.api.importer.FloridaVirtualCampusImporter;
import org.collegeopentextbooks.api.importer.Importer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path="/harvest", 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiHarvestController {

	@Value("${harvest.collegeopentextbooks.inputFolder}")
	private String inputFolder;
	
//	@Autowired
//	private ExampleImporter exampleImporter;
//	
	@Autowired
	private FloridaVirtualCampusImporter floridaVirtualCampusImporter;
	
	@Autowired
	private CotHtmlImporter cotHtmlImporter;
	
	@RequestMapping(method=RequestMethod.GET, value="start")
    @ResponseBody String startHarvesting() {
		// We really only need to import the HTML repository once
		cotHtmlImporter.setInputFolder(new File(inputFolder));
    	List<Importer> importers = new ArrayList<>();
    	importers.add(floridaVirtualCampusImporter);
//    	importers.add(exampleImporter);
    	importers.add(cotHtmlImporter);
    	
    	for(Importer importer: importers) {
    		importer.run();
    	}
    	
    	return "OK";
    }
}