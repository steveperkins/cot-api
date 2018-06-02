package org.collegeopentextbooks.api.importer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.collegeopentextbooks.api.importer.suny.SunyAuthor;
import org.collegeopentextbooks.api.importer.suny.SunyCategory;
import org.collegeopentextbooks.api.importer.suny.SunyLink;
import org.collegeopentextbooks.api.importer.suny.SunyResource;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.service.OrganizationService;
import org.collegeopentextbooks.api.service.RepositoryService;
import org.collegeopentextbooks.api.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class SunyImporter extends AbstractImporter implements Importer {

	private static final Logger LOG = Logger.getLogger(SunyImporter.class);
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Map<Tag, List<String>> tagCategories;
	
	private String baseUrl = "https://textbooks.opensuny.org/wp-json/wp/v2/posts";
	
	private Repository repository;
	
	private Map<String, SunyAuthor> authors;
	
	public SunyImporter() {}
	
	@PostConstruct
	public void init() {
		// SUNY is unique so far in that they provide their own categories for their resources
		// Here we're mapping our category names to their category names
		
		tagCategories = new HashMap<Tag, List<String>>();
		List<String> categories = new ArrayList<String>();
		categories.add("Technology");
		categories.add("electrical engineering");
		categories.add("engineering");
		tagCategories.put(new Tag(null, "Engineering and Electronics"), categories);
		
		categories = new ArrayList<String>();
		categories.add("programming");
		tagCategories.put(new Tag(null, "Computer and Information Science"), categories);
		
		categories = new ArrayList<String>();
		categories.add("Mathematics");
		categories.add("math");
		categories.add("logic");
		categories.add("logical principles");
		categories.add("modal principles");
		tagCategories.put(new Tag(null, "Mathematics"), categories);
		
		categories = new ArrayList<String>();
		categories.add("healthcare");
		tagCategories.put(new Tag(null, "Health and Nursing"), categories);
		
		categories = new ArrayList<String>();
		categories.add("English and Communication");
		categories.add("English");
		categories.add("writing");
		categories.add("poetry");
		categories.add("contemporary poets");
		tagCategories.put(new Tag(null, "English and Composition"), categories);
		
		categories = new ArrayList<String>();
		categories.add("anthropology");
		categories.add("evolution");
		categories.add("paleoanthropology");
		categories.add("poetry");
		categories.add("contemporary poets");
		tagCategories.put(new Tag(null, "Anthropology and Archaeology"), categories);
		
		categories = new ArrayList<String>();
		categories.add("character development");
		categories.add("cinema");
		categories.add("cinematography");
		categories.add("directing style");
		categories.add("director");
		categories.add("film");
		categories.add("genre");
		categories.add("movie editing");
		categories.add("movie production");
		categories.add("movies");
		categories.add("narrative structure");
		categories.add("plot");
		categories.add("theme");
		tagCategories.put(new Tag(null, "Fine Arts"), categories);
		
		categories = new ArrayList<String>();
		categories.add("Social Sciences");
		categories.add("arguments");
		categories.add("critical thinking");
		tagCategories.put(new Tag(null, "Sociology"), categories);
		
		repository = repositoryService.getRepository(getName());

		if(null == repository) {
			LOG.error("Repository not found for " + getName() + ", creating it now");
			Organization organization = organizationService.getOrganization(getName());
			if(null == organization) {
				LOG.info("Organization not found for " + getName() + ", creating it now");
				organization = new Organization();
				organization.setName(getName());
				organization.setUrl("https://textbooks.opensuny.org");
				organization.setLogoUrl("https://textbooks.opensuny.org/wp-content/uploads/Website-Header-July-2017_OST-SOS-new.png");
				organization = organizationService.save(organization);
				LOG.debug("Organization " + getName() + " created with ID " + organization.getId());
			}
			
			repository = new Repository();
	    	repository.setName(getName());
	    	repository.setOrganization(organization);
	    	repository.setUrl(baseUrl);
	    	repository.setLastImportedDate(Date.from(LocalDateTime.of(1970, 01, 01, 0, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant()));
	    	repositoryService.save(repository);
	    	LOG.info("Repository " + getName() + " created with ID " + repository.getId());
		}
		
		authors = new HashMap<String, SunyAuthor>();
	}

	@Override
	public void run() {
		if(null == repository) {
			LOG.error("Repository not found");
			throw new NullPointerException("Repository can't be null");
		}
		if(null == repository.getLastImportedDate()) {
			Calendar cal = Calendar.getInstance();
			cal.roll(Calendar.YEAR, -5);
			repository.setLastImportedDate(cal.getTime());
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = dateFormatter.format(repository.getLastImportedDate());
		LOG.info("Only considering resources created or modified after " + fromDate);
		
		String response = restTemplate.getForObject(baseUrl, String.class);
		LOG.info("Response: " + response);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			List<SunyResource> resources = Arrays.asList(mapper.readValue(response, SunyResource[].class));
			for(SunyResource sunyResource : resources) {
				if(sunyResource.getDate().before(repository.getLastImportedDate())
						&& (null != sunyResource.getModified() && sunyResource.getModified().before(repository.getLastImportedDate()))) {
					LOG.info("SUNY resource with ID " + sunyResource.getId() + " will not be processed because its created or last modified date indicates it has not changed");
					continue;
				}
				
				Resource resource = new Resource();
				resource.setExternalId(sunyResource.getId());
				
				// TODO Determine if this resource has been deleted. We'll probably have to store a set of resource IDs
				// found in the repository, then compare that against what we have in the database and delete the outstanding IDs.
				
				// Set resource title. If no title provided, skip.
				String title = sunyResource.getTitle().getRendered();
				if(StringUtils.isBlank(title)) {
					// No name means no way to reference this resource, so we'll skip it
					LOG.info("Skipping resource ID " + resource.getExternalId() + " because it has no title attribute");
					continue;
				}
				resource.setTitle(title.replaceAll("&#038;", "&").replaceAll("&#8217;", "'").replaceAll("&#8220;", "\"").replaceAll("&#8221;", "\"").replaceAll("&#8211;", "-"));
				
				// Set the URI to this resource
				resource.setUrl(sunyResource.get_links().getSelf().get(0).getHref());
				resource.setUpdatedDate(sunyResource.getModified());
				
				// Iterate through authors and add them to the resource
				List<SunyLink> sunyAuthors = sunyResource.get_links().getAuthor();
				if(!CollectionUtils.isEmpty(sunyAuthors)) {
					resource.setAuthors(new ArrayList<Author>());
					for(SunyLink sunyAuthor: sunyAuthors) {
						SunyAuthor resolvedAuthor = authors.get(sunyAuthor.getHref());
						if(null == resolvedAuthor) {
							// Look up this author's name
							resolvedAuthor = restTemplate.getForObject(sunyAuthor.getHref(), SunyAuthor.class);
							authors.put(sunyAuthor.getHref(), resolvedAuthor);
						}
						resource.getAuthors().add(new Author(resolvedAuthor.getName()));
					}
				}
				
				// Parse out the license information
				if(null != sunyResource.getAcf().getLicense()) {
					// Try to find the start of a Creative Commons license key
					int index = sunyResource.getAcf().getLicense().indexOf("CC ");
					if(index > -1) {
						resource.setLicense(new License(sunyResource.getAcf().getLicense().substring(index), null));
					}
				}
				
				// Attempt to figure out what disciplines this work belongs to
				List<SunyCategory> tags = sunyResource.getPure_taxonomies().getTags();
				for(SunyCategory tag: tags) {
					for(Map.Entry<Tag, List<String>> entry: tagCategories.entrySet()) {
						if(entry.getValue().contains(tag.getName())) {
							if(null == resource.getTags()) { resource.setTags(new ArrayList<Tag>()); }
							resource.getTags().add(entry.getKey());
						}
					}
				}
				
				resource = save(resource);
				
				LOG.info("Title: " + resource.getTitle());
				LOG.info("Identifier: " +  resource.getExternalId());
				LOG.info("Content URL: " + resource.getUrl());
				LOG.info("Authors: " + resource.getAuthors().size());
				if(null != resource.getLicense()) {
					LOG.info("License: " + resource.getLicense().getName());
				}
				LOG.info("");
			}
		} catch (Exception e) {
			LOG.error(e);
		}
		
		// Update the last imported date and perform any other repository-specific teardown
		onFinish();
	}
		
	
	protected Resource save(Resource resource) {
		resource.setRepository(repository);
		return resourceService.save(resource);
	}

	protected void onFinish() {
		// Update the last imported date
		repository.setLastImportedDate(new Date());
		repositoryService.save(repository);
	}

	@Override
	public String getName() {
		return "SUNY";
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}

}
