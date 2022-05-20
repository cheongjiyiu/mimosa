$(document).ready(function() {
	load_redirect();
});

function query(key) {
    key = key.replace(/[*+?^$.\[\]{}()|\\\/]/g, "\\$&");
    var match = $("#navbar-input").val().trim().match(new RegExp("[?&]" + key + "=([^&]+)(&|$)"));
    return match && decodeURIComponent(match[1].replace(/\+/g, " "));
}

function load_redirect() {
	$(".nav-redirect").click(function(e) {
		e.preventDefault();
		
		let href = $(this).attr("href");
		$("#navbar-input").val("https://samantha.iscool.com" + href);
		
		$("#challenge-form").submit();
	});
}

function update_image() {
	let imgSrc = query("img");
	if (imgSrc)
		imgSrc = imgSrc.replace(/script/ig, "");
	$(".bio-img").html("<img src=\"/external/pages/challenges/biography/" + imgSrc + "\" />");
}

function biography_success(response) {
	$(".site-content").html(response.data.page);
	load_redirect();
	update_image();
	default_challenge_success(response);
}

function biography_error(response) {
	default_challenge_error(response);
}
