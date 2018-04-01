package org.collegeopentextbooks.api.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.collegeopentextbooks.api.db.AuthorDao;
import org.collegeopentextbooks.api.model.Author;
import org.collegeopentextbooks.api.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class AuthorDaoImpl implements AuthorDao {
	
	private static String GET_AUTHORS_SQL = "SELECT a.* FROM author a";
	private static String GET_AUTHOR_BY_ID_SQL = "SELECT a.* FROM author a WHERE a.id=?";
	private static String GET_AUTHOR_BY_SEARCH_TERM_SQL = "SELECT a.* FROM author a WHERE a.search_name=? and a.repositoryId=?";
	private static String GET_AUTHORS_BY_RESOURCE_SQL = "SELECT a.* FROM resource_author ra INNER JOIN author a ON ra.author_id=a.id WHERE ra.resource_id=?";
	private static String UPDATE_SQL = "UPDATE author SET name=?, search_name=? WHERE id=?";
	
	private static String DELETE_AUTHOR_FROM_RESOURCE_SQL = "DELETE FROM resource_author WHERE resource_id=? AND author_id=?";
	private static String ADD_AUTHOR_TO_RESOURCE_SQL = DELETE_AUTHOR_FROM_RESOURCE_SQL + "; INSERT INTO resource_author (resource_id, author_id) VALUES(?, ?)";
	
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insert;
	private BeanPropertyRowMapper<Author> rowMapper = BeanPropertyRowMapper.newInstance(Author.class);
	
	@Autowired
	public AuthorDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource)
			                .withTableName("author")
			                .usingGeneratedKeyColumns("id");
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#getAuthors()
	 */
	@Override
	public List<Author> getAuthors() {
		List<Author> results = jdbcTemplate.query(GET_AUTHORS_SQL, rowMapper);
		if(null == results) {
			results = new ArrayList<Author>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#getById(java.lang.Integer)
	 */
	@Override
	public Author getById(Integer authorId) {
		Author author = jdbcTemplate.queryForObject(GET_AUTHOR_BY_ID_SQL, new Integer[] { authorId }, rowMapper);
		return author;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#getBySearchTerm(java.lang.String)
	 */
	@Override
	public Author getBySearchTerm(int repositoryId, String name) {
		List<Author> authors = jdbcTemplate.query(GET_AUTHOR_BY_SEARCH_TERM_SQL, new Object[] { name.toLowerCase(), repositoryId }, rowMapper);
		if(null != authors && !authors.isEmpty()) {
			return authors.get(0);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#getAuthorsByResourceId(java.lang.Integer)
	 */
	@Override
	public List<Author> getAuthorsByResourceId(Integer resourceId) {
		List<Author> results = jdbcTemplate.query(GET_AUTHORS_BY_RESOURCE_SQL, new Integer[] { resourceId }, rowMapper);
		if(null == results) {
			results = new ArrayList<Author>();
		}
		return results;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#addAuthorToResource(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void addAuthorToResource(Integer resourceId, Integer authorId) {
		this.jdbcTemplate.update(ADD_AUTHOR_TO_RESOURCE_SQL, resourceId, authorId, resourceId, authorId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#deleteAuthorFromResource(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void deleteAuthorFromResource(Integer resourceId, Integer authorId) {
		this.jdbcTemplate.update(DELETE_AUTHOR_FROM_RESOURCE_SQL, resourceId, authorId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#merge(org.collegeopentextbooks.api.model.Resource, java.util.List<Author>)
	 */
	@Override
	public List<Author> merge(Resource resource, List<Author> authors) {
		List<Author> finalAuthors = new ArrayList<Author>();
		if(!CollectionUtils.isEmpty(authors)) {
			for(Author author: authors) {
				Author dbAuthor = getBySearchTerm(author.getRepositoryId(), author.getSearchName());
				if(null == dbAuthor) {
					dbAuthor = save(author);
				}
				// Author is now guaranteed to have an ID
				finalAuthors.add(dbAuthor);
			}
			
			// Determine which authors have been added to this resource
			List<Author> newAuthors = new ArrayList<Author>(authors);
			newAuthors.removeAll(resource.getAuthors());
			for(Author author: newAuthors) {
				addAuthorToResource(resource.getId(), author.getId());
			}
			
			// Determine which authors have been removed
			resource.getAuthors().removeAll(finalAuthors);
			// Disassociate the removed authors from this resource
			for(Author author: resource.getAuthors()) {
				deleteAuthorFromResource(resource.getId(), author.getId());
			}
		}
		resource.setAuthors(finalAuthors);
		return resource.getAuthors();
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.db.impl.AuthorDao#save(org.collegeopentextbooks.api.model.Author)
	 */
	@Override
	public Author save(Author author) {
		if(null == author.getId())
			return insert(author);
		else
			return update(author);
	}
	
	protected Author insert(Author author) {
		Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("repositoryId", author.getRepositoryId());
        parameters.put("name", author.getName());
        parameters.put("search_name", author.getName().toLowerCase());
        Number newId = this.insert.executeAndReturnKey(parameters);
        author.setId(newId.intValue());
        return author;
	}
	
	protected Author update(Author author) {
		this.jdbcTemplate.update(UPDATE_SQL, author.getName(), author.getSearchName(), author.getId());
		return author;
	}

}
