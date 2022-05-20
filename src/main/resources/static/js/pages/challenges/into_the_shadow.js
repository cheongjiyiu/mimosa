function shadow_validate(data) {
	if (data.username !== "shadow" || !isPassword(data.password)) {
		$(".wrong-password").html("Incorrect username/password!");
		$("#shadow-form").trigger("reset");
		return false;
	} else {
		return true;
	}
}

function shadow_success(response) {
	default_challenge_success(response);
}

function shadow_error(response) {
	default_challenge_error(response);
}

function shadow_postfilter(form) {
	form.reset();
}
