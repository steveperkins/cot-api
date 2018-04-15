package org.collegeopentextbooks.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.collegeopentextbooks.api.importer.BcCampusImporter;
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

	private static final Logger LOG = Logger.getLogger(ApiHarvestController.class);
	
	@Value("${harvest.collegeopentextbooks.inputFolder}")
	private String inputFolder;
	
	@Autowired
	private FloridaVirtualCampusImporter floridaVirtualCampusImporter;
	
	@Autowired
	private BcCampusImporter bcCampusImporter;
	
	@Autowired
	private CotHtmlImporter cotHtmlImporter;
	
	@RequestMapping(method=RequestMethod.GET, value="start")
    @ResponseBody String startHarvesting() {
		LOG.info("Import starting");
		
		// We really only need to import the HTML repository once
		cotHtmlImporter.setInputFolder(new File(inputFolder));
    	List<Importer> importers = new ArrayList<>();
    	importers.add(bcCampusImporter);
    	importers.add(floridaVirtualCampusImporter);
    	importers.add(cotHtmlImporter);
    	
    	for(Importer importer: importers) {
    		LOG.info("Running importer for " + importer.getName());
    		importer.run();
    		LOG.info("Importer for " + importer.getName() + " Finished");
    	}
    	
    	LOG.info("Import finished");
    	
    	return "OK";
    }
}