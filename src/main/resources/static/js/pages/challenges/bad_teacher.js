function role_based_login_validate(data) {
	if (data.username === "" || data.password === "") {
		$(".wrong-password").html("Please enter your log in credentials.");
	} else {
		return true;
	}
}

function role_based_login_success(response) {
	$(".task-panel-body").html(response);
	$("#bad-teacher-form").simplifySubmit();
}

function role_based_login_error(response) {
	$(".wrong-password").html("Failed to log in! Please try again.");
	$("#bad-teacher-login").trigger("reset");
}
