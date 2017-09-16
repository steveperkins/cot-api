package org.collegeopentextbooks.api.model;

/**
 * A tag - abitrary text used to categorize books and other materials
 * @author Steve
 *
 */
public class Tag {
	private Long id;
	private String name;
	
	public Tag() { }
	public Tag(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}