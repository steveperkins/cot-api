package org.collegeopentextbooks.api.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.service.RepositoryService;
import org.collegeopentextbooks.api.service.ResourceService;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FloridaVirtualCampusImporter extends OaiHarvestImporter implements Importer {

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	private Map<Tag, List<String>> tagKeywords;
	private Repository repository;
	
	public FloridaVirtualCampusImporter() {
		// Orange Grove Resources
		super.collectionIds.add("84c90413-70e2-30e2-948f-a654d751f94f");
		// Open Textbooks
		super.collectionIds.add("779795a7-e183-4c2d-8302-000c16a69023");
		// TODO Investigate "harvested resources" collection
		// Harvested Resources
//		super.collectionIds.add("52fa4d20-6a4d-7706-cedf-6b6daf2e875f");
		
		super.baseUrl = "https://florida.theorangegrove.org/og/oai";
	}
	
	@PostConstruct
	public void init() {
		tagKeywords = tagService.getAllKeywords();
		repository = repositoryService.getRepository("Orange Grove");
	}

	@Override
	protected Resource save(Resource resource) {
		resource.setRepository(repository);
		return resourceService.save(resource);
	}

	@Override
	protected List<License> parseLicenses(String text) {
		// This implementation only considers Creative Commons licensing
		List<License> licenses = new ArrayList<License>();
		if(StringUtils.isBlank(text))
			return licenses;
		
		int index = text.indexOf("CC0");
		if(index >= 0) {
			// Public domain, which trumps other licenses
			licenses.add(new License("CC"));
			licenses.add(new License("PD"));
			return licenses;
		}
		
		index = text.indexOf("CC ");
		if(index >= 0) {
			licenses.add(new License("CC"));
			
			// Look for any further CC sublicenses
			text = text.substring(index + 3);
			index = text.indexOf(" ");
			if(index > 1) {
				text = text.substring(0, index);
				
				while(index >= 0) {
					// Dashes denote additional sublicenses
					index = text.indexOf("-");
					if(index >= 1) {
						String license = text.substring(0, index);
						// All sublicenses are two characters
						if(license.length() == 2) {
							licenses.add(new License(license));
						}
						text = text.substring(index + 1);
						index = text.indexOf("-");
					}
				}
			}
		}
		index = text.indexOf("CC-");
		if(index >= 0) {
			licenses.add(new License("CC"));
			
			// Look for any further CC sublicenses
			text = text.substring(index + 3);
			index = text.indexOf(" ");
			if(index > 1) {
				text = text.substring(0, index);
				
				while(index >= 0) {
					// Dashes denote additional sublicenses
					index = text.indexOf("-");
					if(index >= 1) {
						String license = text.substring(0, index);
						// All sublicenses are two characters
						if(license.length() == 2) {
							licenses.add(new License(license));
						}
						text = text.substring(index + 1);
						index = text.indexOf("-");
					}
				}
			}
		}
		return licenses;
	}

	@Override
	protected List<Tag> analyzeDisciplines(List<String> keywords) {
		List<Tag> matches = new ArrayList<>();
		for(String keyword : keywords) {
			keyword = keyword.trim();
			for(Map.Entry<Tag, List<String>> entry : tagKeywords.entrySet()) {
				// Loop through the tags and attempt to find a match for the current keyword
				if(entry.getValue().contains(keyword.toLowerCase())) {
					matches.add(entry.getKey());
				}
			}
		}
		return matches;
	}

}
