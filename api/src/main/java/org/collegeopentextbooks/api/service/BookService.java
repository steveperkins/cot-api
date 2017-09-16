package org.collegeopentextbooks.api.service;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.model.Book;
import org.collegeopentextbooks.api.model.Tag;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	
	public Book getBook(Long id) {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("A Brief History of Muggles");
		book.setContentUrl("http://www.google.com");
		book.setLicense("CC");
		book.setLicenseUrl("http://www.linkedin.com");
		book.setReviewUrl("http://www.amazon.com");
		
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(1L, "Canonical"));
		tags.add(new Tag(2L, "Biology"));
		tags.add(new Tag(3L, "Literature"));
		book.setTags(tags);
		
		return book;
	}
	
	public List<Book> getBooksByTag(Long tagId) {
		List<Book> books = new ArrayList<Book>();
		books.add(getBook(1L));
		return books;
	}
	
}