$(document).ready(function() {
	$('#forgotpw-form').simplifyValidate({
		password : {
			required : true,
			minlength : 8
		},
		confirmPassword : {
			equalTo : "#password-input"
		}
	}, {
		password : {
			required : "Please provide a password",
			minlength : "Your password must be at least 8 characters long"
		},
		confirmPassword : {
			equalTo : "Your passwords do not match"
		}

	});
});

function reset_validate(data) {
	return $("#forgotpw-form").valid();
}

function reset_success(response) {
	$("#forgotpw-form").simplifyResetForm();

	$.notify({
		icon : "fa fa-check",
		title : "Email sent!",
		title : "Password updated!",
		message : "The password for the user with the username of '"
				+ response.username + "' has been changed!"
	}, {
		type : "success",
		allow_dismiss : false
	});
}

function reset_error(response) {
	if (response.verified === "false") {
		$.notify({
			icon : "fa fa-times",
			title : "Failed to reset password!",
			message : "Please verify the account first!"
		}, {
			type : "danger",
			allow_dismiss : false
		});
	} else {
		$.notify({
			icon : "fa fa-times",
			title : "Failed to reset password!",
			message : "An error occurred!"
		}, {
			type : "danger",
			allow_dismiss : false
		});
	}
}
