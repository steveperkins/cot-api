package org.collegeopentextbooks.api.controller;

import java.util.List;

import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.model.Review;
import org.collegeopentextbooks.api.model.ReviewType;
import org.collegeopentextbooks.api.service.ResourceService;
import org.collegeopentextbooks.api.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
		path={ "/resource", "/resources" }, 
		consumes={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
		produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class ApiResourceController {

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ReviewService reviewService;
	
	@RequestMapping(method=RequestMethod.GET, value="/")
    @ResponseBody List<Resource> getResources() {
        return resourceService.getResources();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{resourceId}")
    @ResponseBody Resource getResource(Integer resourceId) {
        return resourceService.getResource(resourceId);
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/tag/{tagId}")
    @ResponseBody List<Resource> getResourcesByTag(Integer tagId) {
        return resourceService.getResourcesByTag(tagId);
    }
	
	@RequestMapping(method=RequestMethod.GET, value="{resourceId}/reviews/{reviewType}")
    @ResponseBody List<Review> getReviews(Integer resourceId, ReviewType reviewType) {
        return reviewService.getReviews(resourceId, reviewType);
    }
	
}