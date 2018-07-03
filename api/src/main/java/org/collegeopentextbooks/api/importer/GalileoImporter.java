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
public class GalileoImporter extends OaiHarvestImporter implements Importer {

	private static final Logger LOG = Logger.getLogger(GalileoImporter.class);
	private ResourceService resourceService;
	private TagService tagService;
	private RepositoryService repositoryService;
	private OrganizationService organizationService;
	
	private Map<Tag, List<String>> tagKeywords;
	
	public GalileoImporter(@Autowired ResourceService resourceService, 
			@Autowired TagService tagService, 
			@Autowired RepositoryService repositoryService, 
			@Autowired OrganizationService organizationService) {
		super(resourceService);

		// All USG Open Textbooks
//		super.collectionIds.add("publication:all-textbooks");
		// Fine Arts Open Textbooks
		super.collectionIds.add("publication:arts-textbooks");
		// Biological Sciences Open Textbooks
		super.collectionIds.add("publication:biology-textbooks");
		// Business Administration, Management, and Economics Open Textbooks
		super.collectionIds.add("publication:business-textbooks");
		// Chemistry Open Textbooks
		super.collectionIds.add("publication:chemistry-textbooks");
		// Computer Science and Information Technology Open Textbooks
		super.collectionIds.add("publication:compsci-textbooks");
		// ducation Open Textbooks
		super.collectionIds.add("publication:education-textbooks");
		// Engineering Open Textbooks
		super.collectionIds.add("publication:engineering-textbooks");
		// English Open Textbooks
		super.collectionIds.add("publication:english-textbooks");
		// First-Year Experience Open Textbooks
		super.collectionIds.add("publication:fye-textbooks");
		// Geological Sciences and Geography Open Textbooks
		super.collectionIds.add("publication:geo-textbooks");
		// Nursing and Health Sciences Open Textbooks
		super.collectionIds.add("publication:health-textbooks");
		// History Open Textbooks
		super.collectionIds.add("publication:history-textbooks");
		// Information Literacy Open Textbooks
		super.collectionIds.add("publication:infolit-textbooks");
		// Open Textbook
		super.collectionIds.add("publication:material-textbook");
		// Mathematics Open Textbooks
		super.collectionIds.add("publication:mathematics-textbooks");
		// Physics and Astronomy Open Textbooks
		super.collectionIds.add("publication:physics-textbooks");
		// Political Science Open Textbooks
		super.collectionIds.add("publication:polisci-textbooks");
		// Psychology, Sociology, Anthropology, and Social Work Open Textbooks
		super.collectionIds.add("publication:psychology-textbooks");
		
		super.baseUrl = "https://oer.galileo.usg.edu/do/oai/";
		
		this.resourceService = resourceService;
		this.tagService = tagService;
		this.repositoryService = repositoryService;
		this.organizationService = organizationService;
	}
	
	@PostConstruct
	public void init() {
		tagKeywords = tagService.getAllKeywords();
		repository = repositoryService.getRepository("Galileo");

		if(null == repository) {
			LOG.info("Repository not found for Galileo, creating it now");
			Organization organization = organizationService.getOrganization("University of Georgia");
			if(null == organization) {
				LOG.info("Organization not found for University of Georgia, creating it now");
				organization = new Organization();
				organization.setName("University of Georgia");
				organization.setUrl("https://usg.edu");
				organization.setLogoUrl("https://www.usg.edu/assets/global/images/bor_logos/bor_logo.svg");
				organization = organizationService.save(organization);
				LOG.debug("Organization created with ID " + organization.getId());
			}
			
			repository = new Repository();
	    	repository.setName("Galileo");
	    	repository.setOrganization(organization);
	    	repository.setUrl("https://oer.galileo.usg.edu/do/oai/");
	    	repository.setLastImportedDate(Date.from(LocalDateTime.of(1970, 01, 01, 0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant()));
	    	repositoryService.save(repository);
	    	LOG.debug("Repository created with ID " + repository.getId());
		}
		repository.setUrl("https://oer.galileo.usg.edu/do/oai/");
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
		return "University of Georgia Galileo";
	}

}
