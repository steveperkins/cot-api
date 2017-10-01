package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.AuthorDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	
	@Autowired
	private AuthorDao authorDao;
	
	/**
	 * Retrieves ALL authors
	 * @return
	 * @author steve.perkins
	 */
	public List<Author> getAuthors() {
		List<Author> authors = authorDao.getAuthors();
		return authors;
	}
	
	/**
	 * Retrieves an author by it's ID
	 * @param authorId
	 * @return
	 * @author steve.perkins
	 */
	public Author getAuthor(Integer authorId) {
		Author author = authorDao.getById(authorId);
		return author;
	}
	
	/**
	 * Creates or updates the given author's scalar values.
	 * @param repository the author to create or update
	 * @return the updated author. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @author steve.perkins
	 */
	public Author save(Author author) throws RequiredValueEmptyException, ValueTooLongException {
		if(null == author)
			return null;

		if(StringUtils.isBlank(author.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(author.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		return authorDao.save(author);
	}
	
}
