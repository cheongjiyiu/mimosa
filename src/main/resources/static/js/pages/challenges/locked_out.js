function attendance_success(response) {
	$(".code-info").html(response.data.reply);
	default_challenge_success(response);
}

function attendance_error(response) {
	$(".code-info").html(response.data.reply);
	default_challenge_error(response);
}

function attendance_postfilter(form) {
	form.reset();
}
