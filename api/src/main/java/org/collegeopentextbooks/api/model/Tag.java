package org.collegeopentextbooks.api.model;

import java.util.List;

/**
 * A tag - arbitrary text used to categorize books and other materials
 * @author steve.perkins
 *
 */
public class Tag extends AbstractModelObject {
	private String name;
	private TagType tagType;
	private String searchName;
	private Integer parentTagId;
	private List<Tag> children;
	
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
	public Integer getParentTagId() {
		return parentTagId;
	}
	public void setParentTagId(Integer parentTagId) {
		this.parentTagId = parentTagId;
	}
	public List<Tag> getChildren() {
		return children;
	}
	public void setChildren(List<Tag> children) {
		this.children = children;
	}
}
