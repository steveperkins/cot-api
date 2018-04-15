package org.collegeopentextbooks.api.importer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Repository;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.service.ResourceService;
import org.dom4j.Element;
import org.dom4j.Node;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;

public abstract class OaiHarvestImporter implements Importer {

	private static final Logger LOG = Logger.getLogger(OaiHarvestImporter.class);
	protected String baseUrl;
	protected Repository repository;
	protected List<String> collectionIds = new ArrayList<String>();
	
	protected ResourceService resourceService;
	
	protected OaiHarvestImporter(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	/**
	 * Assumes the implementation subclass has populated the repository property with the appropriate repo from the database
	 */
	@Override
	public void run() {
		if(null == repository) {
			LOG.error("Repository should be supplied by subclass implementing OaiHarvestImporter, but was null");
			throw new NullPointerException("Repository can't be null. Set the appropriate repository when initializing your OaiHarvestImporter subclass.");
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = dateFormatter.format(repository.getLastImportedDate());
		String untilDate = dateFormatter.format(new Date());
		
		OaiPmhServer server = new OaiPmhServer(baseUrl);
		for(String collectionId: collectionIds) {
			try {
				RecordsList recordsList = server.listRecords("oai_dc", fromDate, untilDate, collectionId);
				while(null != recordsList) {
					List<Record> records = recordsList.asList();
					for(Record record: records) {
						Resource resource = new Resource();
						
						Header header = record.getHeader();
						resource.setExternalId(header.getIdentifier());
						
						Element metadata = record.getMetadata();
						if(null == metadata && StringUtils.isNotBlank(resource.getExternalId())) {
							// This resource has been deleted. The OAI-PMH library we're using to abstract away some of the 
							// convolution of the OAI-PMH framework doesn't support the status attribute that can be set
							// on the <header> element. The spec says that if a resource has been deleted, the response 
							// for that resource must not include a <metadata> element. To avoid reimplementing the
							// existing library, we make the assumption here that the repository matches the spec.
							// http://www.openarchives.org/OAI/openarchivesprotocol.html#DeletedRecords
							resourceService.delete(resource);
							continue;
						}
						
						String xml = metadata.asXML();
						LOG.info("Current XML: ");
						LOG.info(xml);
						
						resource.setTitle(parseTitle(xml));
						if(StringUtils.isBlank(resource.getTitle())) {
							// No name means no way to reference this resource, so we'll skip it
							LOG.debug("Skipping resource ID " + resource.getExternalId() + " because it has no title attribute");
							continue;
						}
						
						resource.setAuthors(parseAuthors(xml));
						
						String license = parseLicense(xml);
						if(StringUtils.isNotBlank(license)) {
							String upper = license.toUpperCase();
							if(upper.contains("CC BY-NC-SA")
									|| upper.contains("BY-NC-SA"))
								license = "CC BY-NC-SA";
							else if(upper.contains("CC BY-NC-ND")
									|| upper.contains("BY-NC-ND"))
								license = "CC BY-NC-ND";
							else if(upper.contains("CC BY-NC")
									|| upper.contains("BY-NC"))
								license = "CC BY-NC";
							else if(upper.contains("CC BY-ND")
									|| upper.contains("BY-ND"))
								license = "CC BY-ND";
							else if(upper.contains("CC BY-SA")
									|| license.contains("BY-SA"))
								license = "CC BY-SA";
							else if(upper.contains("CC BY"))
								license = "CC BY";
							else if(upper.contains("GNU FREE DOCUMENTATION LICENSE"))
								license = "GFDL";
							resource.setLicense(new License(license));
						}
						
						// TODO Parse out custom license URL
						
						resource.setUrl(parseContentUrl(xml));
						
						// Attempt to figure out what disciplines this work belongs to by doing keyword analysis
						String subject = parseSubject(xml);
						if(null == subject) {
							// Subject was not provided. Try to use Description field to get keywords instead.
							subject = parseDescription(xml);
						}
						if(null != subject) {
							// The subject appears to always be a comma-separated list of values
							List<String> keywords = Arrays.asList(subject.split(","));
							resource.setTags(analyzeDisciplines(keywords));
						}
						
						resource = save(resource);
						
						LOG.debug("Title: " + resource.getTitle());
						LOG.debug("Identifier: " +  resource.getExternalId());
						LOG.debug("Content URL: " + resource.getUrl());
						LOG.debug("Authors: " + resource.getAuthors().size());
						if(null != resource.getLicense()) {
							LOG.debug("License: " + resource.getLicense().getName());
						}
						LOG.debug("");
					}
					if(null != recordsList.getResumptionToken()) {
						recordsList = server.listRecords(recordsList.getResumptionToken());
					} else {
						recordsList = null;
					}
				}
			} catch (OAIException e) {
				e.printStackTrace();
			}
		}
		
		// Update the last imported date and perform any other repository-specific teardown
		onFinish();
	}
	
	/**
	 * Parses through freeform license text to find standardized licensing indicators
	 * @param text
	 * @return
	 * @author steve.perkins
	 */
	protected List<License> parseLicenses(String text) {
		// This implementation only considers Creative Commons licensing
		List<License> licenses = new ArrayList<License>();
		if(StringUtils.isBlank(text)) {
			LOG.debug("Empty license text encountered. No licenses will be set for this resource.");
			return licenses;
		}
		
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
	
	/**
	 * Implementors should override this method to merge each new record into the data store
	 * @param resource
	 * @author steve.perkins
	 */
	protected abstract Resource save(Resource resource);
	
	/**
	 * Uses a list of keywords to determine what discipline tags should be applied to this resource
	 * @param keywords
	 * @return
	 * @author steve.perkins
	 */
	protected abstract List<Tag> analyzeDisciplines(List<String> keywords);
	
	protected List<Tag> analyzeDisciplines(List<String> keywords, Map<Tag, List<String>> tagKeywords) {
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
	
	
	/**
	 * TODO Deal with XML namespaces appropriately instead of taking the slow approach
	 * @param context
	 * @param paths
	 * @return
	 * @author steve.perkins
	 */
	protected Node getByHierarchy(Node context, String...paths) {
		StringBuilder sb = new StringBuilder();
		sb.append("/");
		for(String path: paths) {
			sb.append("/*[local-name()='").append(path).append("']");
		}
		return context.createXPath(sb.toString()).selectSingleNode(context);
	}
	
	protected List<Author> getAuthors(List<Node> creators) {
		List<Author> authors = new ArrayList<Author>();
		if(null == creators || creators.isEmpty())
			return authors;
		
		for(Node node: creators) {
			Author author = new Author(node.getText().trim());
			authors.add(author);
		}
		return authors;
	}
	
/*	List<Author> parseAuthors(String creators) {
		List<Author> authors = new ArrayList<Author>();
		if(StringUtils.isBlank(creators))
			return authors;
		
		for(String authorStr: creators.split(",")) {
			Author author = new Author(authorStr.trim());
			// TODO See if an author by this name already exists in the DB
			authors.add(author);
		}
		return authors;
	}*/
	
	protected String parseContentUrl(String xml) {
		return parseTag("identifier", xml);
	}
	
	protected String parseTitle(String xml) {
		return parseTag("title", xml);
	}
	
	protected String parseLicense(String xml) {
		return parseTag("rights", xml);
	}
	
	protected List<Author> parseAuthors(String xml) {
		List<Author> authors = new ArrayList<Author>();
		
		String marker = "<dc:creator>";
		String[] split = xml.split("</dc:creator>");
		for(String creator: split) {
			int index = creator.indexOf(marker);
			if(index < 0)
				continue;
			
			creator = creator.substring(index + marker.length());
			Author author = new Author(creator.trim());
			authors.add(author);
		}
		return authors;
	}
	
	protected String parseSubject(String xml) {
		return parseTag("subject", xml);
	}
	
	protected String parseDescription(String xml) {
		return parseTag("description", xml);
	}
	
	protected String parseTag(String tagName, String xml) {
		// Check for empty element
		if(xml.indexOf("<dc:" + tagName + "/>") >= 0 || xml.indexOf("<dc:" + tagName + " />") >= 0) {
			return null;
		}
		
		// Check for missing element
		String marker = "<dc:" + tagName + ">";
		int index = xml.indexOf(marker);
		if(index < 0)
			return null;
		
		String value = xml.substring(index + marker.length());
		value = value.substring(0, value.indexOf("</dc:" + tagName + ">"));
		return value;
	}
	
	/**
	 * Called when the importer has successfully imported all available resources from the current repository.
	 * Implement this event to update the repository's last imported date and perform any other teardown needed
	 * for your specific repository.
	 * 
	 * @author steve.perkins
	 */
	protected abstract void onFinish();

}
