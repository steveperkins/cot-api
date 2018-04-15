package org.collegeopentextbooks.api.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.parser.CotHtmlParser;
import org.collegeopentextbooks.api.service.AuthorService;
import org.collegeopentextbooks.api.service.EditorService;
import org.collegeopentextbooks.api.service.OrganizationService;
import org.collegeopentextbooks.api.service.RepositoryService;
import org.collegeopentextbooks.api.service.ResourceService;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CotHtmlImporter implements Importer {

	private static final Logger LOG = Logger.getLogger(CotHtmlImporter.class);
	
	@Autowired
	private ResourceService resourceService;
	
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private EditorService editorService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private TagService tagService;
    
	private File inputFolder;
	
	public void setInputFolder(File inputFolder) {
		this.inputFolder = inputFolder;
	}

	@Override
	public void run() {
		File[] inputFiles = inputFolder.listFiles();
		
		Organization organization = organizationService.getOrganization(getName());
		if(null == organization) {
			organization = new Organization();
	    	organization.setName(getName());
	    	organization.setUrl("http://collegeopentextbooks.org");
	    	organization.setLogoUrl("http://www.collegeopentextbooks.org/images/logo-inner.png");
	    	organization = organizationService.save(organization);
		}
		
		Repository repository = repositoryService.getRepository(getName());
		if(null == repository) {
			repository = new Repository();
	    	repository.setName(getName());
	    	repository.setOrganization(organization);
	    	repository.setUrl("http://www.collegeopentextbooks.org");
	    	repositoryService.save(repository);
		}
		
		CotHtmlParser parser = new CotHtmlParser();
		for(File inputFile: inputFiles) {
			try {
				List<Resource> parsedResources = parser.parse(inputFile);
				for(Resource resource: parsedResources) {
					System.out.println("Resource name: " + resource.getTitle());
					System.out.println("Tag count: " + resource.getTags().size());
					resource.setRepository(repository);
					resource = resourceService.save(resource);
					
					if(null != resource.getAuthors()) {
						List<Author> authors = new ArrayList<Author>(resource.getAuthors());
						for(Author author: authors) {
							author.setRepositoryId(repository.getId());
							authorService.save(author);
							resourceService.addAuthorToResource(resource, author);
						}
					}
					if(null != resource.getEditors()) {
						for(Editor editor: resource.getEditors()) {
							editorService.save(editor);
							resourceService.addEditorToResource(resource, editor);
						}
					}
					
					if(null != resource.getTags()) {
						List<Tag> tags = new ArrayList<Tag>(resource.getTags());
						for(Tag tag: tags) {
							tagService.save(tag);
							resourceService.addTagToResource(resource, tag);
						}
					}
					
				}
			} catch (IOException e) {
				LOG.error("Could not parse " + inputFile.getAbsolutePath(), e);
			}
		}
	}

	@Override
	public String getName() {
		return "College Open Textbooks";
	}
}
