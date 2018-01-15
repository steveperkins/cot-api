package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/review", "/reviews" }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiReviewController {

	@Autowired
	private ReviewService reviewServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET, value="")
	@ResponseBody List<Review> getReviews() {
		return reviewServiceImpl.getReviews();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="{reviewId}")
    @ResponseBody Review getReview(@PathVariable Integer reviewId) {
        return reviewServiceImpl.getReview(reviewId);
    }
	
	
}