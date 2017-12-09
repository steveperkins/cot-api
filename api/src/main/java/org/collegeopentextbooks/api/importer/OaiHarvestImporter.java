package org.collegeopentextbooks.api.importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.util.CollectionUtils;

import se.kb.oai.OAIException;
import se.kb.oai.pmh.Header;
import se.kb.oai.pmh.OaiPmhServer;
import se.kb.oai.pmh.Record;
import se.kb.oai.pmh.RecordsList;

public abstract class OaiHarvestImporter implements Importer {

	protected String baseUrl;
	protected List<String> collectionIds = new ArrayList<String>();
			
	@Override
	public void run() {
		OaiPmhServer server = new OaiPmhServer(baseUrl);
		for(String collectionId: collectionIds) {
			try {
				RecordsList recordsList = server.listRecords("oai_dc", "2005-01-01", "2017-12-31", collectionId);
				while(null != recordsList) {
					List<Record> records = recordsList.asList();
					for(Record record: records) {
						Resource resource = new Resource();
						
						Header header = record.getHeader();
						resource.setExternalId(header.getIdentifier());
						
						Element metadata = record.getMetadata();
						String xml = metadata.asXML();
						System.out.println();
						System.out.println("Current XML:");
						System.out.println(xml);
						
						resource.setTitle(parseTitle(xml));
						if(StringUtils.isBlank(resource.getTitle())) {
							// No name means no way to reference this resource, so we'll skip it
							System.out.println("Skipping resource ID " + resource.getExternalId() + " because it has no title attribute");
							continue;
						}
						
						resource.setAuthors(parseAuthors(xml));
						
						String license = parseLicense(xml);
						if(StringUtils.isNotBlank(license)) {
							List<License> licenses = parseLicenses(license);
							if(!CollectionUtils.isEmpty(licenses)) {
								resource.setLicenses(licenses);
							}
						}
						
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
						
						System.out.println("Title: " + resource.getTitle());
						System.out.println("Identifier: " +  resource.getExternalId());
						System.out.println("Content URL: " + resource.getUrl());
						System.out.println("Authors: " + resource.getAuthors().size());
						System.out.println("License: " + license);
						if(null != resource.getLicenses()) {
							System.out.print("Licenses: ");
							for(License l: resource.getLicenses()) {
								System.out.print(l.getId() + " ");
							}
						}
						System.out.println();
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
	}
	
	/**
	 * Parses through freeform license text to find standardized licensing indicators
	 * @param text
	 * @return
	 * @author steve.perkins
	 */
	protected abstract List<License> parseLicenses(String text);
	
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

}
