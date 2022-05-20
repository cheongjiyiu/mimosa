$(document).ready(function() {
	$('#forgotpw-form').simplifyValidate({
		email : {
			required : true,
			email : true
		}
	}, {
		email : {
			required : "Please enter your email.",
			email : "Please enter a valid email address."
		}
	});
});

function reset_validate(data) {
	return $("#forgotpw-form").valid();
}

function reset_success(response) {
	$("#forgotpw-form").simplifyResetForm();

	$
			.notify(
					{
						icon : "fa fa-check",
						title : "Email sent!",
						message : "An email will be sent to you shortly with a link to reset your password"
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
