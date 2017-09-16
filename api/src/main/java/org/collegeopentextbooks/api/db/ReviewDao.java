package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.BookReview;
import org.springframework.stereotype.Component;

@Component
public class ReviewDao {
	
	public List<BookReview> getReviews(Long bookId) {
		return null;
	}

	public BookReview getReview(Long id) {
		return null;
	}
}
