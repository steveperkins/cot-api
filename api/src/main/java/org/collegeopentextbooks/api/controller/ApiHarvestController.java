package org.collegeopentextbooks.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.importer.CotHtmlImporter;
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
		consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiHarvestController {

	@Value("${harvest.collegeopentextbooks.inputFolder}")
	private String inputFolder;
	
//	@Autowired
//	private ExampleImporter exampleImporter;
//	
//	@Autowired
//	private FloridaVirtualCampusImporter floridaVirtualCampusImporter;
	
	@Autowired
	private CotHtmlImporter cotHtmlImporter;
	
	@RequestMapping(method=RequestMethod.GET, value="start")
    @ResponseBody String startHarvesting() {
		cotHtmlImporter.setInputFolder(new File(inputFolder));
    	List<Importer> importers = new ArrayList<>();
    	importers.add(cotHtmlImporter);
//    	importers.add(floridaVirtualCampusImporter);
//    	importers.add(exampleImporter);
    	
    	for(Importer importer: importers) {
    		importer.run();
    	}
    	
    	return "OK";
    }
}