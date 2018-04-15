package org.collegeopentextbooks.api.importer;

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
public class BcCampusImporter extends OaiHarvestImporter implements Importer {

	private static final Logger LOG = Logger.getLogger(BcCampusImporter.class);
	private ResourceService resourceService;
	private TagService tagService;
	private RepositoryService repositoryService;
	private OrganizationService organizationService;
	
	private Map<Tag, List<String>> tagKeywords;
	
	public BcCampusImporter(@Autowired ResourceService resourceService, 
			@Autowired TagService tagService, 
			@Autowired RepositoryService repositoryService, 
			@Autowired OrganizationService organizationService) {
		super(resourceService);

		// BC Campus Open Textbook Collection
		super.collectionIds.add("7567d816-90cc-4547-af7a-3dbd43277639");
		// Learning Object- Creative Commons License
		super.collectionIds.add("049adcac-fc00-30a2-6229-5ed52267d29e");
		// Tool or Technology - GPL
		super.collectionIds.add("4245d416-da80-5edf-198e-eb7028149743");
		// Full Course - Creative Commons License
		super.collectionIds.add("e208cc56-dc68-9482-5459-697beed9247a");
		// OAI from UofC -LOCC License
		super.collectionIds.add("5937e05a-affd-44e6-8d51-940e87f15572");
		// with the World (Creative Commons License)
		super.collectionIds.add("7260a846-d0ee-44e6-b79d-661dca074d0b");
		// Provincial Government Creative Commons Resources
		super.collectionIds.add("0b3fbf2e-5652-464b-9505-5ce326e32c07");
		// Alberta OER
		super.collectionIds.add("f1953436-3cc8-4bb8-bd02-44cd5386c4df");
		
		super.baseUrl = "http://solr.bccampus.ca:8001/bcc/oai";
		
		this.resourceService = resourceService;
		this.tagService = tagService;
		this.repositoryService = repositoryService;
		this.organizationService = organizationService;
	}
	
	@PostConstruct
	public void init() {
		tagKeywords = tagService.getAllKeywords();
		repository = repositoryService.getRepository(getName() + " SOLR");

		if(null == repository) {
			LOG.info("Repository not found for " + getName() + ", creating it now");
			Organization organization = organizationService.getOrganization(getName());
			if(null == organization) {
				LOG.info("Organization not found for " + getName() + ", creating it now");
				organization = new Organization();
				organization.setName(getName());
				organization.setUrl("https://bccampus.ca");
				organization.setLogoUrl("https://bccampus.ca/wp-content/themes/wordpress-bootstrap-child/images/bccampus-logo.png");
				organization = organizationService.save(organization);
				LOG.debug("Organization " + getName() + " created with ID " + organization.getId());
			}
			
			repository = new Repository();
	    	repository.setName(getName()  + "SOLR");
	    	repository.setOrganization(organization);
	    	repository.setUrl("http://solr.bccampus.ca:8001/bcc/oai");
	    	repositoryService.save(repository);
	    	LOG.debug("Repository " + getName() + " created with ID " + repository.getId());
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
		return "BC Campus";
	}

}
