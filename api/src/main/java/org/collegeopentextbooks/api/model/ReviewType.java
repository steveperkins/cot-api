package org.collegeopentextbooks.api.model;

public enum ReviewType {
	CONTENT,
	ACCESSIBILITY;
	
	public static ReviewType fromString(String str) {
		if(str == null)
			return null;
		
		str = str.toUpperCase();
		for(ReviewType reviewType: ReviewType.values()) {
			if(reviewType.toString().equals(str))
				return reviewType;
		}
		
		return null;
	}
}
