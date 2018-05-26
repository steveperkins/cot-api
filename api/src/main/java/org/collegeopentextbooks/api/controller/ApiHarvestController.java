package org.collegeopentextbooks.api.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.collegeopentextbooks.api.importer.CotHtmlImporter;
import org.collegeopentextbooks.api.importer.FloridaVirtualCampusImporter;
import org.collegeopentextbooks.api.importer.Importer;
import org.collegeopentextbooks.api.model.ImportStatus;
import org.collegeopentextbooks.api.model.ImportStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
	
	@Value("${harvest.importers}")
	private String importerClasses;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping(method=RequestMethod.GET, value="start")
    @ResponseBody ImportStatusResponse startHarvesting() {
		LOG.info("Import starting");
		
		List<Importer> importers = new ArrayList<>();
		LOG.info("Parsing importer list");
		for(String importerClass: importerClasses.split(",")) {
			String[] names = applicationContext.getBeanNamesForType(FloridaVirtualCampusImporter.class);
			Importer importer = (Importer)applicationContext. getAutowireCapableBeanFactory().getBean(importerClass);
			
			// COT HTML importer is a special animal that requires a file path
			if(importerClass.toLowerCase().endsWith("cothtmlimporter")) {
				((CotHtmlImporter)importer).setInputFolder(new File(inputFolder));
			}
			
			importers.add(importer);
		}
		
    	for(Importer importer: importers) {
    		LOG.info("Running importer for " + importer.getName());
    		importer.run();
    		LOG.info("Importer for " + importer.getName() + " Finished");
    	}
    	
    	LOG.info("Import finished");
    	
    	return new ImportStatusResponse(ImportStatus.FINISHED, "Import finished");
    }
}