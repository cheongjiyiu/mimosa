$(document).ready(function() {
	$("#challenge-form").submit();
    load_redirect();
});

function casino_royale_header(xhr) {
	if ($("#navbar-input").val().startsWith("https://royale.casino.ca")) {
		let royale_token = sessionStorage.getItem("royale-token");
		xhr.setRequestHeader("Authorization", `Bearer ${royale_token}`);
	}
}

function load_redirect() {
	$(".nav-redirect.casino-royale").click(function(e) {
		e.preventDefault();
		
		let href = $(this).attr("href");
		$("#navbar-input").val("https://royale.casino.ca" + href);
		
		$("#challenge-form").submit();
	});
}

function casino_royale_success(response) {
	$(".site-content").html(response);
	$(".site-content form").simplifySubmit();
    load_redirect();
}

function default_challenge_header(xhr) {
	if ($("#navbar-input").val().startsWith("https://royale.casino.ca")) {
		let royale_token = sessionStorage.getItem("royale-token");
		xhr.setRequestHeader("Authorization", `Bearer ${royale_token}`);
	}
}
