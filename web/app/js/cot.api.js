function CotClient() {
	CotClient.ROOT_URL = "http://localhost:8080";
}

/**
 * Searches resources using the given criteriaModel.
 * Arguments:
 * 	criteriaModel	one or more hierarchical properties matching org.collegeopentextbooks.api.model.SearchCriteria
 * Returns:
 *  a promise that returns an array of org.collegeopentextbooks.api.model.Resource on success
 */
CotClient.prototype.search = function(criteriaModel) {
	return $.ajax({
		url: CotClient.ROOT_URL + "/search", 
		type: "POST",
		data: JSON.stringify(criteriaModel)
	});
}

/**
 * Retrieves all known authors
 * Returns:
 *  a promise that returns an array of org.collegeopentextbooks.api.model.Author on success
 */
CotClient.prototype.getAllAuthors = function() {
	return $.ajax({
		url: CotClient.ROOT_URL + "/authors",
		type: "GET"
	});
}