$(document).ready(function() {
	$('#password-form').simplifyValidate({
		oldPassword : {
			required : true
		},
		password : {
			required : true,
			minlength : 8
		},
		confirmPassword : {
			equalTo : "#password-input"
		}
	}, {
		oldPassword : {
			required : "Please type your old password"
		},
		password : {
			required : "Please provide a password",
			minlength : "Your password must be at least 8 characters long"
		},
		confirmPassword : {
			required : "Please enter your new password again",
			equalTo : "Your passwords do not match"
		}
	});
});

function change_password_validate(data) {
	return $("#password-form").valid();
}

function change_password_success(response) {
	$("#password-form").simplifyResetForm();

	$.notify({
		icon : "fa fa-check",
		title : "Password changed!",
		message : "The password for '" + response.username
				+ "' has been updated!"
	}, {
		type : "success",
		allow_dismiss : false
	});
}

function change_password_error(response) {
	$.notify({
		icon : "fa fa-times",
		title : "Failed to update user!",
		message : "An error occurred!"
	}, {
		type : "danger",
		allow_dismiss : false
	});
}
