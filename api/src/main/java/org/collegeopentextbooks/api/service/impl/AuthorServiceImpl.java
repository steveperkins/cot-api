package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.AuthorDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {
	
	private static final Integer NAME_MAX_LENGTH = 255;
	
	@Autowired
	private AuthorDao authorDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.AuthorService#getAuthors()
	 */
	@Override
	public List<Author> getAuthors() {
		List<Author> authors = authorDao.getAuthors();
		return authors;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.AuthorService#getAuthor(java.lang.Integer)
	 */
	@Override
	public Author getAuthor(Integer authorId) {
		Author author = authorDao.getById(authorId);
		return author;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.AuthorService#save(org.collegeopentextbooks.api.model.Author)
	 */
	@Override
	public Author save(Author author) throws RequiredValueEmptyException, ValueTooLongException {
		if(null == author)
			return null;

		if(StringUtils.isBlank(author.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(author.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		Author existingAuthor = authorDao.getBySearchTerm(author.getName());
		if(null != existingAuthor) {
			author.setId(existingAuthor.getId());
		}
		return authorDao.save(author);
	}
	
	public void addAuthorToResource(Resource resource, Author author) {
		authorDao.addAuthorToResource(resource.getId(), author.getId());
	}

	@Override
	public List<Author> getAuthors(Resource resource) {
		return authorDao.getAuthorsByResourceId(resource.getId());
	}

}
