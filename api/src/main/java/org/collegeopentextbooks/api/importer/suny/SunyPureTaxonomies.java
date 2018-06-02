package org.collegeopentextbooks.api.importer.suny;

import java.util.List;

public class SunyPureTaxonomies {
	private List<SunyCategory> categories;
	private List<SunyCategory> tags;
	
	public List<SunyCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<SunyCategory> categories) {
		this.categories = categories;
	}
	public List<SunyCategory> getTags() {
		return tags;
	}
	public void setTags(List<SunyCategory> tags) {
		this.tags = tags;
	}
	
}
