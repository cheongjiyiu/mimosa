function search_success(response) {
	$(".search-results").html(response);
}

function search_error(response) {
	$(".search-results").html(response.error);
}

function search_postfilter(form) {
	form.reset();
}
