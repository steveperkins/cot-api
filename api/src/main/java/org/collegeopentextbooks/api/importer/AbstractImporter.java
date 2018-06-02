package org.collegeopentextbooks.api.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.collegeopentextbooks.api.model.Tag;

public class AbstractImporter {
	
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
	
}
