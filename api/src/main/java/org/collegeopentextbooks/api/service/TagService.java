package org.collegeopentextbooks.api.service;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.model.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagService {
	
	public List<Tag> getAll() {
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(1L, "Canonical"));
		tags.add(new Tag(2L, "Biology"));
		tags.add(new Tag(3L, "Literature"));
		
		return tags;
	}
	
}
