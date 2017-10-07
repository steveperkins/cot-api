package org.collegeopentextbooks.api.importer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Resource;
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
	protected Integer repositoryId;
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
						resource.setTitle(parseTitle(xml));
						
						resource.setAuthors(parseAuthors(xml));
						
						String license = parseLicense(xml);
						if(StringUtils.isNotBlank(license)) {
							List<License> licenses = parseLicenses(license);
							if(!CollectionUtils.isEmpty(licenses)) {
								resource.setLicenses(licenses);
							}
						}
						
						resource.setUrl(parseContentUrl(xml));
						
						System.out.println("Title: " + resource.getTitle());
						System.out.println("Identifier: " +  resource.getExternalId());
						System.out.println("Content URL: " + resource.getUrl());
						System.out.println("Authors: " + resource.getAuthors().size());
						System.out.println("License: " + license);
						System.out.print("Licenses: ");
						for(License l: resource.getLicenses()) {
							System.out.print(l.getId() + " ");
						}
						System.out.println();
						System.out.println("XML:");
						System.out.println(metadata.asXML());
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
		String marker = "<dc:identifier>";
		String url = xml.substring(xml.indexOf(marker) + marker.length());
		url = url.substring(0, url.indexOf("</dc:identifier>"));
		return url;
	}
	
	protected String parseTitle(String xml) {
		String marker = "<dc:title>";
		String title = xml.substring(xml.indexOf(marker) + marker.length());
		title = title.substring(0, title.indexOf("</dc:title>"));
		return title;
	}
	
	protected String parseLicense(String xml) {
		// Check for empty element
		if(xml.indexOf("<dc:rights/>") >= 0 || xml.indexOf("<dc:rights />") >= 0) {
			return null;
		}
		
		String marker = "<dc:rights>";
		String url = xml.substring(xml.indexOf(marker) + marker.length());
		url = url.substring(0, url.indexOf("</dc:rights>"));
		return url;
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

}
