package org.collegeopentextbooks.api.importer;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.importer.Importer;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.collegeopentextbooks.api.model.Reviewer;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.service.AuthorService;
import org.collegeopentextbooks.api.service.EditorService;
import org.collegeopentextbooks.api.service.LicenseService;
import org.collegeopentextbooks.api.service.OrganizationService;
import org.collegeopentextbooks.api.service.RepositoryService;
import org.collegeopentextbooks.api.service.ResourceService;
import org.collegeopentextbooks.api.service.ReviewService;
import org.collegeopentextbooks.api.service.ReviewerService;
import org.collegeopentextbooks.api.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleImporter implements Importer {
	
    @Autowired
    private ResourceService resourceServiceImpl;
    
    @Autowired
    private RepositoryService repositoryServiceImpl;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private EditorService editorServiceImpl;
    
    @Autowired
    private OrganizationService organizationServiceImpl;
    
    @Autowired
    private ReviewService reviewServiceImpl;
    
    @Autowired
    private TagService tagServiceImpl;
    
    @Autowired
    private ReviewerService reviewerServiceImpl;
    
    @Autowired
    private LicenseService licenseServiceImpl;
    
    @Override
    public void run() {
    	List<License> licenses = licenseServiceImpl.getAll();
    	
    	Organization organization = new Organization();
    	organization.setName("Muggle Myopia");
    	organization.setUrl("http://www.amazon.com");
    	organization.setLogoUrl("http://www.amazon.com/muggles.png");
    	organizationServiceImpl.save(organization);
    	
    	List<Author> authors = new ArrayList<Author>();
    	Author author = new Author();
    	author.setName("Henry Higgins");
    	authors.add(author);
    	authorService.save(author);
    	
    	List<Editor> editors = new ArrayList<Editor>();
    	Editor editor = new Editor();
    	editor.setName("Alfred Hitchcock");
    	editors.add(editor);
    	editorServiceImpl.save(editor);
    	
    	Repository repository = new Repository();
    	repository.setName("College Open Textbooks");
    	repository.setOrganization(organization);
    	repository.setUrl("http://www.collegeopentextbooks.org");
    	repositoryServiceImpl.save(repository);
    	
    	Resource resource = new Resource();
    	resource.setRepository(repository);
    	resource.setTitle("Muggles in the Wild");
    	resource.setUrl("http://www.google.com/books/Muggles-in-the-Wild");
    	resource.setExternalReviewUrl("http://www.amazon.com/book-review");
    	resource.setAncillariesUrl("http://www.collegeopentextbooks.org/ancillaries");
    	resourceServiceImpl.save(resource);
    	
    	List<License> resourceLicenses = new ArrayList<License>();
    	resourceLicenses.add(new License("CC", "Creative Commons"));
    	resourceLicenses.add(new License("BY", "Attribution"));
    	
    	for(Author currentAuthor: authors) {
    		resourceServiceImpl.addAuthorToResource(resource, currentAuthor);
    	}
    	for(Editor currentEditor: editors) {
    		resourceServiceImpl.addEditorToResource(resource, currentEditor);
    	}
    	for(License license: resourceLicenses) {
    		if(!licenses.contains(license)) {
    			license = licenseServiceImpl.insert(license);
    		}
    		resourceServiceImpl.addLicenseToResource(resource, license.getId());
    	}
    	resource.setAuthors(authors);
    	resource.setEditors(editors);
    	resource.setLicenses(resourceLicenses);
    	
    	
    	Tag tag = tagServiceImpl.getByName("Literature");
    	resourceServiceImpl.addTagToResource(resource, tag);
    	
    	Reviewer reviewer = new Reviewer();
    	reviewer.setName("Mike Pouraryan");
    	reviewer.setTitle("Adjunct Faculty");
    	reviewer.setOrganization(organization);
    	reviewer.setBiography("I have had over 18 years experience in Operations, Finance & Adminstration for Small to Medium Size Businesses, start-ups and publicly held companies. I have also served as an Adjunct Professor for a number of years with a special focus on Management & Public Policy. I also serve as principal moderator for the \"Weekly Outsider\".");
    	reviewerServiceImpl.save(reviewer);
    	
    	Review review = new Review();
    	review.setResource(resource);
    	review.setReviewer(reviewer);
    	review.setReviewType(ReviewType.CONTENT);
    	review.setScore(4.8);
    	review.setChartUrl("http://www.collegeopentextbooks.org/images/Reviews/business ethics.png");
    	review.setComments("I recommend this textbook as primary textbook for both associate and bachelor level programs. It began with some hypothetical text cases which were tough but it laid out a critical decision making process. The dialogues on Ethical Decision Making and Corporate Social Governance are more necessary than ever. I was looking to bring something into the classroom that focused on the here and this text provides that. I think introducing essential definitions a little earlier could be helpful.");
    	reviewServiceImpl.save(review);
    }

}
