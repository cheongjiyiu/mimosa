$(document).ready(function() {
	load_redirect();
});


function load_redirect() {
	$(".nav-redirect").click(function(e) {
		e.preventDefault();
		
		let href = $(this).attr("href");
		$("#navbar-input").val("https://dism.edu.sg" + href);
		
		$("#challenge-form").submit();
	});
}

function directory_traversal_success(response) {
	$(".site-content").html(response.data.page);
	load_redirect();
	default_challenge_success(response);
}

function directory_traversal_error(response) {
	default_challenge_error(response);
}
