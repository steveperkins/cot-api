package org.collegeopentextbooks.api.model;

/**
 * A tag - arbitrary text used to categorize books and other materials
 * @author Steve
 *
 */
public class Tag extends AbstractModelObject {
	private String name;
	private TagType tagType;
	private String searchName;
	
	public Tag() { }
	public Tag(Integer id, String name) {
		this(id, name, TagType.GENERAL);
	}
	public Tag(Integer id, String name, TagType tagType) {
		this.id = id;
		this.name = name;
		this.tagType = tagType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		if(null != name)
			setSearchName(name.toLowerCase());
	}
	public TagType getTagType() {
		return tagType;
	}
	public void setTagType(TagType tagType) {
		this.tagType = tagType;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
}
