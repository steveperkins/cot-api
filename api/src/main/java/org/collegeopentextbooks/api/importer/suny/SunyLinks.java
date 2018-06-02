package org.collegeopentextbooks.api.importer.suny;

import java.util.List;

public class SunyLinks {
	private List<SunyLink> self;
	private List<SunyLink> author;
	
	public List<SunyLink> getSelf() {
		return self;
	}
	public void setSelf(List<SunyLink> self) {
		this.self = self;
	}
	public List<SunyLink> getAuthor() {
		return author;
	}
	public void setAuthor(List<SunyLink> author) {
		this.author = author;
	}
	
}
