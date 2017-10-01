package org.collegeopentextbooks.api.model;

public enum TagType {
	GENERAL,
	DISCIPLINE,
	SUB_DISCIPLINE;
	
	public static TagType fromString(String str) {
		if(str == null)
			return null;
		
		str = str.toUpperCase();
		for(TagType tagType: TagType.values()) {
			if(tagType.toString().equals(str))
				return tagType;
		}
		
		return null;
	}
}
