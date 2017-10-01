package org.collegeopentextbooks.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.collegeopentextbooks.api.service.ResourceService;
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
		path={ "/resource", "/resources" }, 
		consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiResourcesController {

	@Autowired
	private ResourceService resourceServiceImpl;

	@Autowired
	private ReviewService reviewServiceImpl;
	
	@RequestMapping(method=RequestMethod.GET, value="")
    @ResponseBody List<Resource> getResources() {
        return resourceServiceImpl.getResources();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{resourceId}")
    @ResponseBody Resource getResource(@PathVariable Integer resourceId) {
        return resourceServiceImpl.getResource(resourceId);
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/tag/{tagId}")
    @ResponseBody List<Resource> getResourcesByTag(@PathVariable Integer tagId) {
        return resourceServiceImpl.getResourcesByTag(tagId);
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{resourceId}/reviews/{reviewTypeString}")
    @ResponseBody List<Review> getReviews(@PathVariable Integer resourceId, 
    		@PathVariable String reviewTypeString) {
		ReviewType reviewType = ReviewType.fromString(reviewTypeString);
		if(null == reviewType)
			return new ArrayList<Review>();
		
        return reviewServiceImpl.getReviews(resourceId, reviewType);
    }
	
}