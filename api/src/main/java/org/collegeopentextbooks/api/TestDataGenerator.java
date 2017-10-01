package org.collegeopentextbooks.api;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@ComponentScan("org.collegeopentextbooks.api")
@PropertySource("classpath:application.properties")
public class TestDataGenerator {
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(TestDataGenerator.class);
        try {
	        TestDataGenerator generator = context.getBean(TestDataGenerator.class);
	        generator.start();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
    }

	@Value("${spring.datasource.url}")
	private String datasourceUrl;
	
	@Value("${spring.datasource.username}")
	private String datasourceUsername;
	
	@Value("${spring.datasource.password}")
	private String datasourcePassword;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setUrl(datasourceUrl);
		datasource.setUsername(datasourceUsername);
		datasource.setPassword(datasourcePassword);
		return datasource;
	}
	
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
    
    public void start() {
    	addRepository1();
    	addRepository2();
    }
    
    public void addRepository1() {
    	List<License> licenses = licenseServiceImpl.getAll();
    	
    	Organization organization = createOrganization("Muggle Myopia", "http://www.amazon.com", "http://www.amazon.com/muggles.png");
    	
    	List<Author> authors = new ArrayList<Author>();
    	authors.add(createAuthor("Henry Higgins"));
    	
    	List<Editor> editors = new ArrayList<Editor>();
    	editors.add(createEditor("Alfred Hitchcock"));
    	editors.add(createEditor("Slim Shady"));
    	
    	Repository repository = createRepository("College Open Textbooks", organization, "http://www.collegeopentextbooks.org");
    	
    	Resource resource = createResource("Muggles in the Wild", "http://www.google.com/books/Muggles-in-the-Wild", "http://www.amazon.com/book-review", "http://www.collegeopentextbooks.org/ancillaries", repository);
    	
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
    	
    	Reviewer reviewer = createReviewer("Mike Pouraryan", "Adjunct Faculty", 
    			"I have had over 18 years experience in Operations, Finance & Adminstration for Small to Medium Size Businesses, start-ups and publicly held companies. I have also served as an Adjunct Professor for a number of years with a special focus on Management & Public Policy. I also serve as principal moderator for the \"Weekly Outsider\".", 
    			organization);
    	
    	Review review = createReview(ReviewType.CONTENT, 4.8,
    			"http://www.collegeopentextbooks.org/images/Reviews/business ethics.png", 
    			"I recommend this textbook as primary textbook for both associate and bachelor level programs. It began with some hypothetical text cases which were tough but it laid out a critical decision making process. The dialogues on Ethical Decision Making and Corporate Social Governance are more necessary than ever. I was looking to bring something into the classroom that focused on the here and this text provides that. I think introducing essential definitions a little earlier could be helpful.", 
    			resource, 
    			reviewer);
    	
    	Resource resource2 = createResource("Intrinsic Vocabulaire", "http://www.google.com/books/Intrinsic-Vocabulaire", "http://www.amazon.com/book-review", "http://www.collegeopentextbooks.org/ancillaries", repository);
    	Author author = createAuthor("Henry Liddicoat");
    	resourceServiceImpl.addAuthorToResource(resource2, author);
    	resourceServiceImpl.addLicenseToResource(resource, "CC");
    }
    
    public void addRepository2() {
    	Organization organization = createOrganization("Uber Test Organization", "http://www.google.com", "http://www.google.com/uber.png");
    	
    	List<Author> authors = new ArrayList<Author>();
    	authors.add(createAuthor("Louise Llamas"));
    	
    	Repository repository = createRepository("Myanmar Textbook Repo", organization, "http://www.myanmartextbookrepo.org");
    	
    	Resource resource = createResource("Simply Fine Cooking", "http://www.google.com/books/simplyfinecooking", "http://www.amazon.com/book-review", "http://www.myanmartextbookrepo.com/ancillaries", repository);
    	
    	List<License> resourceLicenses = new ArrayList<License>();
    	resourceLicenses.add(new License("CC", "Creative Commons"));
    	
    	for(Author currentAuthor: authors) {
    		resourceServiceImpl.addAuthorToResource(resource, currentAuthor);
    	}
    	for(License license: resourceLicenses) {
    		resourceServiceImpl.addLicenseToResource(resource, license.getId());
    	}
    	resource.setAuthors(authors);
    	resource.setLicenses(resourceLicenses);
    	
    	resourceServiceImpl.addTagToResource(resource, tagServiceImpl.getByName("Law"));
    	resourceServiceImpl.addTagToResource(resource, tagServiceImpl.getByName("Philosophy"));
    	resourceServiceImpl.addTagToResource(resource, tagServiceImpl.getByName("Science"));
    }
    
    private Organization createOrganization(String name, String url, String logoUrl) {
    	Organization organization = new Organization();
    	organization.setName(name);
    	organization.setUrl(url);
    	organization.setLogoUrl(logoUrl);
    	return organizationServiceImpl.save(organization);
    }

    private Author createAuthor(String name) {
    	Author author = new Author();
    	author.setName(name);
    	return authorService.save(author);
    }
    
    private Editor createEditor(String name) {
    	Editor editor = new Editor();
    	editor.setName(name);
    	return editorServiceImpl.save(editor);
    }
    
    private Repository createRepository(String name, Organization organization, String url) {
    	Repository repository = new Repository();
    	repository.setName(name);
    	repository.setUrl(url);
    	repository.setOrganization(organization);
    	return repositoryServiceImpl.save(repository);
    }
    
    private Resource createResource(String title, String url, String externalReviewUrl, String ancillariesUrl, Repository repository) {
    	Resource resource = new Resource();
    	resource.setRepository(repository);
    	resource.setTitle(title);
    	resource.setUrl(url);
    	resource.setExternalReviewUrl(externalReviewUrl);
    	resource.setAncillariesUrl(ancillariesUrl);
    	return resourceServiceImpl.save(resource);
    }
    
    private Reviewer createReviewer(String name, String title, String biography, Organization organization) {
    	Reviewer reviewer = new Reviewer();
    	reviewer.setName(name);
    	reviewer.setTitle(title);
    	reviewer.setOrganization(organization);
    	reviewer.setBiography(biography);
    	return reviewerServiceImpl.save(reviewer);
    }
    
    private Review createReview(ReviewType reviewType, Double score, String chartUrl, String comments, Resource resource, Reviewer reviewer) {
    	Review review = new Review();
    	review.setResource(resource);
    	review.setReviewer(reviewer);
    	review.setReviewType(reviewType);
    	review.setScore(score);
    	review.setChartUrl(chartUrl);
    	review.setComments(comments);
    	return reviewServiceImpl.save(review);
    }
}
