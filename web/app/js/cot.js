function formatResourceHtml(resource) {
	var html = "<resourceTitle>" + resource.title + "</resourceTitle>"
	if(resource.authors && resource.authors.length > 0) {
		html += " by <authorName>" + resource.authors[0].name + "</authorName>";
		if(resource.authors.length > 1) {
			html += " +" + (resource.authors.length - 1) + " more";
		}
	}
	if(resource.licenses && resource.licenses.length > 0) {
		html +=" ";
		$.each(resource.licenses, function(idx, license) {
			html += "<license alt='" + license.description + "'>" + license.id + "</license>"; 
		});
	}
	if(resource.ancillariesUrl || resource.externalReviewUrl) {
		html += "<p>"
		if(resource.ancillariesUrl) {
			html += " <a href='" + resource.ancillariesUrl + "'>Ancillaries</a>"
		}
		if(resource.externalReviewUrl) {
			if(resource.ancillariesUrl)
				html += " |";
			
			html += " <a href='" + resource.externalReviewUrl + "'>Review</a>"
		}
		html += "</p>"
	}
	return html;
}

$( document ).ready(function() {
	$.ajaxSetup({ contentType: "application/json", dataType: "json", headers: { "Content-Type" : "application/json" } });
		
	var apiClient = new CotClient();
	
	var btnGetAllAuthors = $("#btnGetAllAuthors");
	btnGetAllAuthors.click(function() {
		apiClient.getAllAuthors()
		.done(function(data) {
			btnGetAllAuthors.siblings(".action-results-count").text(data.length);
			var actionResults = btnGetAllAuthors.siblings(".action-results");
			actionResults.css("display", "block");
			var actionResultsData = actionResults.find(".action-results-data");
			
			$.each(data, function(index, result) {
				actionResultsData.append($("<div/>", {
				    className: "result",
				    html: "<authorName>" + result.name + "</authorName>"
				}));
			})
	    })
	    .fail(function(xhr) {
	      console.error("Could not retrieve authors", xhr);
	    });
	});
	
	var btnSearchByAuthor = $("#btnSearchByAuthor");
	btnSearchByAuthor.click(function() {
		apiClient.search({ authorIds: [383] })
		.done(function(data) {
			btnSearchByAuthor.siblings(".action-results-count").text(data.length);
			var actionResults = btnSearchByAuthor.siblings(".action-results");
			actionResults.css("display", "block");
			var actionResultsData = actionResults.find(".action-results-data");
			
			$.each(data, function(index, result) {
				actionResultsData.append($("<div/>", {
					className: "result",
					html: formatResourceHtml(result)
				}));
			})
		})
		.fail(function(xhr) {
			console.error("Could not search resources by author", xhr);
		});
	});
	
	var btnSearchByTitle = $("#btnSearchByTitle");
	btnSearchByTitle.click(function() {
		apiClient.search({ partialTitle: "Principles" })
		.done(function(data) {
			btnSearchByTitle.siblings(".action-results-count").text(data.length);
			var actionResults = btnSearchByTitle.siblings(".action-results");
			actionResults.css("display", "block");
			var actionResultsData = actionResults.find(".action-results-data");
			
			$.each(data, function(index, result) {
				actionResultsData.append($("<div/>", {
					className: "result",
					html: formatResourceHtml(result) 
				}));
			})
		})
		.fail(function(xhr) {
			console.error("Could not search resources by title", xhr);
		});
	});
	
	var btnSearchByDiscipline = $("#btnSearchByDiscipline");
	btnSearchByDiscipline.click(function() {
		apiClient.search({ tagIds: [19] })
		.done(function(data) {
			btnSearchByDiscipline.siblings(".action-results-count").text(data.length);
			var actionResults = btnSearchByDiscipline.siblings(".action-results");
			actionResults.css("display", "block");
			var actionResultsData = actionResults.find(".action-results-data");
			
			$.each(data, function(index, result) {
				actionResultsData.append($("<div/>", {
					className: "result",
					html: formatResourceHtml(result) 
				}));
			})
		})
		.fail(function(xhr) {
			console.error("Could not search resources by title", xhr);
		});
	});
	
	var btnSearchByRepository = $("#btnSearchByRepository");
	btnSearchByRepository.click(function() {
		apiClient.search({ repositoryIds: [2] })
		.done(function(data) {
			btnSearchByRepository.siblings(".action-results-count").text(data.length);
			var actionResults = btnSearchByRepository.siblings(".action-results");
			actionResults.css("display", "block");
			var actionResultsData = actionResults.find(".action-results-data");
			
			$.each(data, function(index, result) {
				actionResultsData.append($("<div/>", {
					className: "result",
					html: formatResourceHtml(result) 
				}));
			})
		})
		.fail(function(xhr) {
			console.error("Could not search resources by title", xhr);
		});
	});
});