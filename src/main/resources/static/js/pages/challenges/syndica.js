$(document).ready(function() {
	load_redirect();
	attach_search();
});

function query(key) {
    key = key.replace(/[*+?^$.\[\]{}()|\\\/]/g, "\\$&");
    var match = $("#navbar-input").val().trim().match(new RegExp("[?&]" + key + "=([^&]+)(&|$)"));
    return match && decodeURIComponent(match[1].replace(/\+/g, " "));
}

function attach_search() {
	$("#dism-search-form").submit(function(e) {
		e.preventDefault();
		
		let val = $("#dism-search-input").val();
		$("#navbar-input").val("https://dism.edu.sg/search?q=" + val);
		
		$("#challenge-form").submit();
	});
}

function load_redirect() {
	$(".nav-redirect").click(function(e) {
		e.preventDefault();
		
		let href = $(this).attr("href");
		$("#navbar-input").val("https://dism.edu.sg" + href);
		
		$("#challenge-form").submit();
	});
}

function syndica_success(response) {
	$(".site-content").html(response.data.page);
	load_redirect();
	attach_search();
	default_challenge_success(response);
}

function syndica_error(response) {
	default_challenge_error(response);
}
