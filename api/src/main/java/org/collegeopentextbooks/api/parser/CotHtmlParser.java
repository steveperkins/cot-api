package org.collegeopentextbooks.api.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Editor;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Tag;
import org.collegeopentextbooks.api.model.TagType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CotHtmlParser {

	private final Map<String, String> filenameToDisciplineCategoryMap = new HashMap<String, String>();
	
	public CotHtmlParser() {
		filenameToDisciplineCategoryMap.put("education", "Humanities");
		filenameToDisciplineCategoryMap.put("english and composition", "Humanities");
		filenameToDisciplineCategoryMap.put("finearts", "Humanities");
		filenameToDisciplineCategoryMap.put("history", "Humanities");
		filenameToDisciplineCategoryMap.put("language and communication", "Humanities");
		filenameToDisciplineCategoryMap.put("literature", "Humanities");
		filenameToDisciplineCategoryMap.put("philosophy", "Humanities");
		
		filenameToDisciplineCategoryMap.put("computer and informationscience", "Applied Science");
		filenameToDisciplineCategoryMap.put("engineering and electronics", "Applied Science");
		filenameToDisciplineCategoryMap.put("health and nursing", "Applied Science");
		
		filenameToDisciplineCategoryMap.put("accounting and finance", "Business");
		filenameToDisciplineCategoryMap.put("generalbusiness", "Business");
		
		filenameToDisciplineCategoryMap.put("biology and genetics", "Natural Science");
		filenameToDisciplineCategoryMap.put("chemistry", "Natural Science");
		filenameToDisciplineCategoryMap.put("generalscience", "Natural Science");
		filenameToDisciplineCategoryMap.put("mathematics", "Natural Science");
		filenameToDisciplineCategoryMap.put("physics", "Natural Science");
		filenameToDisciplineCategoryMap.put("statistics and probability", "Natural Science");
		
		filenameToDisciplineCategoryMap.put("anthropology and archaeology", "Social Science");
		filenameToDisciplineCategoryMap.put("economics", "Social Science");
		filenameToDisciplineCategoryMap.put("law", "Social Science");
		filenameToDisciplineCategoryMap.put("politicalscience", "Social Science");
		filenameToDisciplineCategoryMap.put("psychology", "Social Science");
		filenameToDisciplineCategoryMap.put("sociology", "Social Science");
	}
	
	public List<Resource> parse(File inputFile) throws IOException {
		List<Tag> tags = new ArrayList<Tag>();
		// Use the filename as the discipline name
		Tag disciplineTag = new Tag();
		disciplineTag.setTagType(TagType.SUB_DISCIPLINE);
		disciplineTag.setName(inputFile.getName().replace(".html", "").replace("&", " and "));
		tags.add(disciplineTag);
		
		Tag disciplineCategoryTag = new Tag();
		disciplineCategoryTag.setTagType(TagType.DISCIPLINE);
		disciplineCategoryTag.setName(filenameToDisciplineCategoryMap.get(disciplineTag.getName()));
		tags.add(disciplineCategoryTag);
		
		List<Resource> resources = new ArrayList<Resource>();
		Document doc = Jsoup.parse(inputFile, "UTF-8", "http://example.com/");
		// Each rListingTableContentCell contains one book
		Elements resourceElements = doc.getElementsByClass("rListingTableContentCell");
		for(Element resourceElement: resourceElements) {
			// All books are defined mostly by links
			// Get all links related to this book
			Elements links = resourceElement.getElementsByTag("a");
			
			// The first child link contains the book's title and URL to the book's content
			Element bookLink = links.get(0);
			String title = bookLink.ownText();
			// Deutch im Blick and Francais Interactif have an unusual HTML structure. It's not worth writing an exception case just for these weirdos.
			if("Deutsch".equals(title) || "".equals(title))
				continue;
			
			String contentUrl = bookLink.attr("href");
			
			Resource resource = new Resource();
			resource.setTags(tags);
			resource.setTitle(title);
			resource.setUrl(contentUrl);
			
			// Author name, editor name, and text formatting is all huddled in the book div's text with no distinction
			parseAuthors(resource, resourceElement.ownText());
			
			if(links.size() > 1) {
				/* The second link should always be the book's license. A license can be several things:
				 - 2-char code
				 - multi-part Creative Commons license codes
				 - Custom License with custom URL
				 */ 
				Element licenseLink = links.get(1);
				resource.setLicense(parseLicenses(licenseLink));
				
				// If there is a third link, it is either the ancillaries or a review
				if(links.size() > 2) {
					Element thirdLink = links.get(2);
					String text = thirdLink.ownText();
					if("Ancillaries".equals(text)) {
						resource.setAncillariesUrl(thirdLink.attr("href"));
					} else if("Review".equals(text)) {
						resource.setCotReviewUrl(thirdLink.attr("href"));
					}
					
					// Likewise, if there is a fourth link it is the opposite of the 3rd link
					if(links.size() > 3) {
						Element fourthLink = links.get(3);
						text = fourthLink.ownText();
						if("Ancillaries".equals(text)) {
							resource.setAncillariesUrl(fourthLink.attr("href"));
						} else if("Review".equals(text)) {
							resource.setCotReviewUrl(fourthLink.attr("href"));
						}
					}
				}
			}
			resources.add(resource);
		}
		return resources;
	}
	
	/**
	 * Attempts to parse out authors and editors from <code>authors</code> and set them as appropriate on the given <code>resource</code>
	 * @param resource
	 * @param authors
	 * @author steve.perkins
	 */
	private void parseAuthors(Resource resource, String authors) {
		// Try to clean this up
		// Normalize groups of names. Even two-author groups should be separated by a comma instead of "and".
		authors = authors.replace(", and ", ",");
		authors = authors.replace(" and ", ",");
		
		// Get rid of formatting
		authors = authors.replaceAll("[\\(\\)\\[\\]]", "");
		authors = authors.replaceAll("\\p{C}\\p{Z}", " ");
		
		// Check if this is an editor
		boolean isAuthor = true;
		int index = authors.indexOf(" ed. by ");
		if(index > -1) {
			// Oops, this is an editor, not an author
			isAuthor = false;
			authors.replace(" ed. by ", "");
		}
		// This is very weird. The character before "by" is not a space or other whitespace, but a non-printable that I can't identify.
//		authors = authors.replace(" by ", "").trim();
		index = authors.indexOf("by ");
		if(index == 0 || index == 1) {
			// Remove the weirdo "by"
			authors = authors.substring(index + 3);
		}
		authors = authors.trim();
		
		// At this point the text group should be only names (and et al)
		String[] names = authors.split(",");
		
		if(isAuthor) {
			List<Author> authorsList = new ArrayList<Author>();
			for(String name: names) {
				name = name.trim();
				// There is one book that has no author. CK-12 Biology.
				if(StringUtils.isBlank(name))
					name = "UNKNOWN";
				authorsList.add(new Author(name));
			}
			resource.setAuthors(authorsList);
		} else {
			List<Editor> editorsList = new ArrayList<Editor>();
			for(String name: names) {
				editorsList.add(new Editor(name.trim()));
			}
			resource.setEditors(editorsList);
		}
	}
	
	private License parseLicenses(Element licensesLink) {
		String licenseText = licensesLink.ownText();
		if(null != licenseText && licenseText.toUpperCase().contains("CUSTOM"))
			licenseText = "Custom";
		
		String licenseLink = licensesLink.attr("href");
		if(null != licenseLink && licenseLink.startsWith("#"))
			licenseLink = null;
		
		return new License(licenseText, licenseLink);
	}
	
	public String replace(String name, String replaceWords) {

		String replacepreffix = name.replaceAll(replaceWords, "");
		String replacesuffix = replacepreffix.replaceAll("\\[]", "");
		String actualName = replacesuffix.replaceAll("[\\()]", "");
		return (actualName);
	}

	public String getDisicipline(String listingname) {
		String[] listings = listingname.split("/");
		String Disicipline = "";
		for (String listing : listings) {
			if (listing.contains(".html")) {
				// System.out.println(listing);
				Disicipline = listing.replaceAll(".html", "");

			}
		}
		return (Disicipline);
	}

	public static void main(String[] args) {
		String inputFolderPath = "F:/consultingprojects/collegeopentextbooks/listings_html";
		File inputFolder = new File(inputFolderPath);

		File[] inputFiles = inputFolder.listFiles();
		
		StringBuilder sb = null;
		CotHtmlParser parser = new CotHtmlParser();
		for(File inputFile: inputFiles) {
			try {
				for(Resource resource: parser.parse(inputFile)) {
					sb = new StringBuilder();
					sb.append("Resource: {\r\n")
					.append("\r\ntitle:\t").append(resource.getTitle())
					.append("\r\nurl:\t").append(resource.getUrl())
					.append("\r\nancillariesUrl:\t").append(resource.getAncillariesUrl())
					.append("\r\nexternalReviewUrl:\t").append(resource.getCotReviewUrl())
					
					.append("\r\nauthors: [");
					if(null != resource.getAuthors()) {
						for(Author author: resource.getAuthors())
							sb.append("\r\n\tname:\t").append(author.getName());
					}
					sb.append("]")
					.append("\r\neditors: [");
					if(null != resource.getEditors()) {
						for(Editor editor: resource.getEditors())
							sb.append("\r\n\tname:\t").append(editor.getName());
					}
					sb.append("],")
					.append("\r\nlicenses: [");
					if(null != resource.getLicense())
						sb.append("\r\n\tname:\t").append(resource.getLicense());
					sb.append("]");
					System.out.println(sb.toString());
					System.out.println();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}