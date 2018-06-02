package org.collegeopentextbooks.api.importer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.service.OrganizationService;
import org.collegeopentextbooks.api.service.RepositoryService;
import org.collegeopentextbooks.api.service.ResourceService;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FloridaVirtualCampusImporter extends OaiHarvestImporter implements Importer {

	private static final Logger LOG = Logger.getLogger(FloridaVirtualCampusImporter.class);
	private ResourceService resourceService;
	private TagService tagService;
	private RepositoryService repositoryService;
	private OrganizationService organizationService;
	
	private Map<Tag, List<String>> tagKeywords;
	
	public FloridaVirtualCampusImporter(@Autowired ResourceService resourceService, 
			@Autowired TagService tagService, 
			@Autowired RepositoryService repositoryService, 
			@Autowired OrganizationService organizationService) {
		super(resourceService);

		// Orange Grove Resources
		super.collectionIds.add("84c90413-70e2-30e2-948f-a654d751f94f");
		// Open Textbooks
		super.collectionIds.add("779795a7-e183-4c2d-8302-000c16a69023");
		// TODO Investigate "harvested resources" collection
		// Harvested Resources
//		super.collectionIds.add("52fa4d20-6a4d-7706-cedf-6b6daf2e875f");
		
		super.baseUrl = "https://florida.theorangegrove.org/og/oai";
		
		this.resourceService = resourceService;
		this.tagService = tagService;
		this.repositoryService = repositoryService;
		this.organizationService = organizationService;
	}
	
	@PostConstruct
	public void init() {
		tagKeywords = tagService.getAllKeywords();
		repository = repositoryService.getRepository("Orange Grove");

		if(null == repository) {
			LOG.info("Repository not found for Orange Grove, creating it now");
			Organization organization = organizationService.getOrganization("Florida Virtual Campus");
			if(null == organization) {
				LOG.info("Organization not found for Florida Virtual Campus, creating it now");
				organization = new Organization();
				organization.setName("Florida Virtual Campus");
				organization.setUrl("https://www.floridashines.org/orange-grove");
				organization.setLogoUrl("https://www.floridashines.org/floridaShines.org-theme/images/flvc.png");
				organization = organizationService.save(organization);
				LOG.debug("Organization created with ID " + organization.getId());
			}
			
			repository = new Repository();
	    	repository.setName("Florida Virtual Campus");
	    	repository.setOrganization(organization);
	    	repository.setUrl("https://www.floridashines.org/orange-grove");
	    	repository.setLastImportedDate(Date.from(LocalDateTime.of(1970, 01, 01, 0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant()));
	    	repositoryService.save(repository);
	    	LOG.debug("Repository created with ID " + repository.getId());
		}
	}

	@Override
	protected Resource save(Resource resource) {
		resource.setRepository(repository);
		return resourceService.save(resource);
	}

	@Override
	protected List<Tag> analyzeDisciplines(List<String> keywords) {
		return super.analyzeDisciplines(keywords, tagKeywords);
	}

	@Override
	protected void onFinish() {
		// Update the last imported date
		repository.setLastImportedDate(new Date());
		repositoryService.save(repository);
	}

	@Override
	public String getName() {
		return "FLVC Orange Grove";
	}

}
