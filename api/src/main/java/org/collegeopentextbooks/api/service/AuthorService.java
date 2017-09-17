package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.db.AuthorDaoImpl;
import org.collegeopentextbooks.api.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
	
	@Autowired
	private AuthorDaoImpl authorDao;
	
	/**
	 * Retrieves ALL authors
	 * @return
	 */
	public List<Author> getAuthors() {
		List<Author> authors = authorDao.getAuthors();
		return authors;
	}
	
	/**
	 * Retrieves an author by it's ID
	 * @param authorId
	 * @return
	 */
	public Author getAuthor(Integer authorId) {
		Author author = authorDao.getById(authorId);
		return author;
	}
	
	public Author save(Author author) {
		return authorDao.save(author);
	}
	
}
